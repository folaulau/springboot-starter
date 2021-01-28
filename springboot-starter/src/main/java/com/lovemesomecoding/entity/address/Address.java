package com.lovemesomecoding.entity.address;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@DynamicUpdate
@Entity
@Table(name = "address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long              id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String            uuid;

    @Column(name = "street")
    private String            street;

    @Column(name = "street2")
    private String            street2;

    @Column(name = "city")
    private String            city;

    @Column(name = "state")
    private String            state;

    @Column(name = "zipcode")
    private String            zipcode;

    @Column(name = "country")
    private String            country;

    @Column(name = "longitude")
    private Double            longitude;

    @Column(name = "latitude")
    private Double            latitude;

    @Column(name = "timezone")
    private String            timezone;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime     createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime     updatedAt;

    @PrePersist
    private void preCreate() {
        if (this.uuid == null || this.uuid.isEmpty()) {
            this.uuid = "addr-" + UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    private void preUpdate() {

    }

}
