package com.lovemesomecoding.batch.processor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lovemesomecoding.batch.log.JobItem;
import com.lovemesomecoding.batch.log.JobItemRepository;
import com.lovemesomecoding.batch.log.JobStatus;
import com.lovemesomecoding.batch.log.JobType;
import com.lovemesomecoding.entity.user.User;
import com.lovemesomecoding.entity.user.UserService;
import com.lovemesomecoding.utils.ObjMapperUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailPaymentReminderItemProcessor implements ItemProcessor<User, JobItem> {

    @Autowired
    private JobItemRepository jobItemRepository;

    @Autowired
    private UserService       userService;

    @Override
    public JobItem process(User user) throws Exception {
        log.debug("process(..)", ObjMapperUtils.toJson(user));

        JobItem jobItem = new JobItem();
        jobItem.setSubscriptionUuid(UUID.randomUUID().toString());
        jobItem.setUserUuid("user-" + UUID.randomUUID().toString());
        jobItem.setPaymentDate(LocalDate.now());
        jobItem.setStatus(JobStatus.SUCCESS);
        jobItem.setJobType(JobType.SEND_PAYMENT_REMINDER_EMAIL);

        if (user != null) {

            userService.sendMonthlyPaymentReminder(user.getUuid());

            // check if email already sent
            LocalDateTime todayBeginOfDay = LocalDate.now().atTime(LocalTime.MIN);
            LocalDateTime todayEndOfDay = LocalDate.now().atTime(LocalTime.MAX);

            log.info("todayBeginOfDay={},todayEndOfDay={}", todayBeginOfDay.toString(), todayEndOfDay.toString());
            Optional<JobItem> opt = jobItemRepository.findByCreatedAtBetweenAndJobTypeAndUserUuid(todayBeginOfDay, todayEndOfDay, JobType.SEND_PAYMENT_REMINDER_EMAIL, user.getUuid());

            if (opt.isPresent()) {
                log.info("existing job found id={}", opt.get().getId());
            } else {
                log.info("no existing job found");
            }
        }

        log.debug("jobItem", ObjMapperUtils.toJson(jobItem));

        return jobItem;
    }

}
