package com.lovemesomecoding.entity.user.role;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.dto.helper.ApiSession;
import com.lovemesomecoding.entity.user.User;
import com.lovemesomecoding.utils.ApiSessionUtils;

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
@Table(name = "role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long              id;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private Authority         authority;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private Set<User>         users;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime     createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime     updatedAt;

    /*
     * uuid of the user creating this user
     */
    @CreatedBy
    @Column(name = "created_by_user", updatable = false)
    private String            createdByUser;

    /*
     * uuid of the user updating this user
     */
    @LastModifiedBy
    @Column(name = "last_updated_by_user")
    private String            lastUpdatedByUser;

    @Column(name = "last_updated_by_user_type")
    private String            lastUpdatedByUserType;

    public Role(Authority authority) {
        this(null, authority);
    }

    public Role(Long id, Authority authority) {
        this.id = id;
        this.authority = authority;
    }

    @PrePersist
    private void preCreate() {

        ApiSession currentUser = ApiSessionUtils.getApiSession();

        if (currentUser != null) {
            this.createdByUser = currentUser.getUserUuid();
            this.lastUpdatedByUser = currentUser.getUserUuid();
            this.lastUpdatedByUserType = currentUser.getRolesAsStr();
        } else {
            this.createdByUser = "SYSTEM";
            this.lastUpdatedByUser = "SYSTEM";
            this.lastUpdatedByUserType = "SYSTEM";
        }

    }

    @PreUpdate
    private void preUpdate() {
        ApiSession currentUser = ApiSessionUtils.getApiSession();

        if (currentUser != null) {
            this.lastUpdatedByUser = currentUser.getUserUuid();
            this.lastUpdatedByUserType = currentUser.getRolesAsStr();
        } else {
            this.lastUpdatedByUser = "SYSTEM";
            this.lastUpdatedByUserType = "SYSTEM";
        }
    }

}
