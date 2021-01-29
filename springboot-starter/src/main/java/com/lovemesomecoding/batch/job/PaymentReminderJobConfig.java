package com.lovemesomecoding.batch.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import com.lovemesomecoding.batch.log.JobItem;
import com.lovemesomecoding.batch.log.JobItemRepository;
import com.lovemesomecoding.batch.log.JobType;
import com.lovemesomecoding.batch.processor.EmailPaymentReminderItemProcessor;
import com.lovemesomecoding.entity.user.User;
import com.lovemesomecoding.entity.user.UserRepository;
import com.lovemesomecoding.entity.user.UserStatus;

/**
 * 
 * @author folaukaveinga
 * 
 *         For batch jobs, you need 3 things<br>
 *         1. reader(RepositoryItemReader) to read from database what data to process<br>
 *         2. processor(ItemProcessor) to process what the job is meant to do<br>
 *         3. writer(RepositoryItemWriter) to write to file or database<br>
 *         4. step(Step) to build your flow<br>
 *         5. Fire job from BatchJobService<br>
 *
 */
@Configuration
public class PaymentReminderJobConfig {

    @Autowired
    public JobBuilderFactory  jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean(name = "monthlyPaymentReminderJob")
    public Job monthlyPaymentReminderJob(@Autowired @Qualifier("step1") Step step1) {
        return this.jobBuilderFactory.get(JobType.SEND_PAYMENT_REMINDER_EMAIL.name()).incrementer(new RunIdIncrementer()).flow(step1).end().build();
    }

    /**
     * 
     * ***** STEP 1 Find eligible users and insert them into job_item table *****
     * 
     * Get users that have payment due today and insert to job_item table
     */
    @Bean
    public Step step1(@Autowired @Qualifier("emailPaymentReminderItemProcessor") EmailPaymentReminderItemProcessor emailPaymentReminderItemProcessor,
            @Autowired @Qualifier("userReader") RepositoryItemReader<User> userReader, @Autowired @Qualifier("saveJobItemLog") RepositoryItemWriter<JobItem> saveJobItemLog) {

        return stepBuilderFactory.get("step1")
                .<User, JobItem> chunk(5)
                .reader(userReader)
                .processor(emailPaymentReminderItemProcessor)
                .writer(saveJobItemLog)
                .faultTolerant()
                .skipPolicy(new SkipPolicy() {

                    @Override
                    public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
                        if (t instanceof DataIntegrityViolationException) {
                            return true;
                        }
                        return false;
                    }

                })
                .build();
    }

    /**
     * STEP 2 get all eligible users that email will be sent to
     */
    @Bean(name = "userReader")
    @StepScope
    public RepositoryItemReader<User> userReader(@Autowired UserRepository userRepository) {
        RepositoryItemReader<User> reader = new RepositoryItemReader<>();
        reader.setRepository(userRepository);

        List<Object> methodArgs = new ArrayList<Object>();

        methodArgs.add(UserStatus.ACTIVE);

        reader.setArguments(methodArgs);
        reader.setMethodName("findByStatus");
        reader.setSort(Collections.singletonMap("id", Sort.Direction.ASC));

        return reader;
    }

    /**
     * STEP 3 save JobItem for processing
     */
    @Bean(name = "saveJobItemLog")
    public RepositoryItemWriter<JobItem> saveJobItemLog(@Autowired JobItemRepository jobItemRepository) {
        RepositoryItemWriterBuilder<JobItem> builder = new RepositoryItemWriterBuilder<JobItem>();
        builder.repository(jobItemRepository).methodName("saveAndFlush");
        return builder.build();
    }

}
