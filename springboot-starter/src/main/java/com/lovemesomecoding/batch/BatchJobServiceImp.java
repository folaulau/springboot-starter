package com.lovemesomecoding.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BatchJobServiceImp implements BatchJobService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("monthlyPaymentReminderJob")
    private Job         monthlyPaymentReminderJob;

    @Override
    public boolean startMonthlyPaymentReminderJob() {

        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(monthlyPaymentReminderJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.warn(e.getLocalizedMessage());
            e.printStackTrace();
        }

        return true;

    }

}
