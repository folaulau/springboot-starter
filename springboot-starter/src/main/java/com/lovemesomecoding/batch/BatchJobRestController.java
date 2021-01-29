package com.lovemesomecoding.batch;

import static org.springframework.http.HttpStatus.OK;

import javax.validation.Valid;

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lovemesomecoding.batch.log.JobType;
import com.lovemesomecoding.dto.AuthenticationResponseDTO;
import com.lovemesomecoding.dto.SignUpDTO;
import com.lovemesomecoding.entity.user.UserRestController;
import com.lovemesomecoding.utils.ObjMapperUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("/jobs")
@Api(value = "batchjobs", tags = "BatchJobs")
@Slf4j
@RestController
public class BatchJobRestController {

    @Autowired
    private BatchJobService batchJobService;

    @ApiOperation(value = "Monthly Payment Reminder")
    @PostMapping(value = "/monthly-payment-reminder")
    public ResponseEntity<Boolean> sendMonthlyPaymentReminder() {
        log.info("sendMonthlyPaymentReminder()");

        batchJobService.startMonthlyPaymentReminderJob();

        return new ResponseEntity<>(true, OK);
    }

}
