package com.lovemesomecoding.batch.log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobItemRepository extends JpaRepository<JobItem, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<JobItem> findByCreatedAtBetweenAndJobTypeAndUserUuid(LocalDateTime createdAtStart, LocalDateTime createdAtEnd, JobType jobType, String userUuid);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Page<JobItem> findByJobType(JobType jobType, Pageable page);

}
