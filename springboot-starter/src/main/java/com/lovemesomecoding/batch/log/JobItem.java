package com.lovemesomecoding.batch.log;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"subscriptionUuid"})})
public class JobItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long              id;

    @Column
    private String            userUuid;

    @Column
    private String            subscriptionUuid;

    @Column(name = "payment_date")
    private LocalDate         paymentDate;

    @Column
    private String            paymentStatus;

    /**
     * System error
     */
    @Column
    private String            error;

    @Column
    private Double            amount;

    @Column
    @Enumerated(EnumType.STRING)
    private JobType           jobType;

    @Column
    @Enumerated(EnumType.STRING)
    private JobStatus         status;

    @Version
    @Column(name = "VERSION")
    private Integer           version;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime     createdAt;

}
