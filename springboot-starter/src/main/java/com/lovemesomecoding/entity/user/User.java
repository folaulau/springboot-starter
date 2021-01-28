package com.lovemesomecoding.entity.user;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.dto.helper.ApiSession;
import com.lovemesomecoding.entity.address.Address;
import com.lovemesomecoding.entity.listener.UserListener;
import com.lovemesomecoding.entity.user.role.Role;
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
@SQLDelete(sql = "UPDATE user SET deleted = 'T' WHERE id = ?", check = ResultCheckStyle.NONE)
@Where(clause = "deleted = 'F'")
@Table(name = "user", indexes = {@Index(columnList = "uuid"), @Index(columnList = "email"), @Index(columnList = "status")})
public class User implements Serializable {

    private static final long serialVersionUID       = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long              id;

    @Column(name = "uuid", unique = true, nullable = false, updatable = false)
    private String            uuid;

    @Column(name = "first_name")
    private String            firstName;

    @Column(name = "middle_name")
    private String            middleName;

    @Column(name = "last_name")
    private String            lastName;

    @NotEmpty
    @Column(name = "email", unique = true)
    private String            email;

    @NotEmpty
    @Column(name = "password", length = 255, nullable = false)
    private String            password;

    @Type(type = "true_false")
    @Column(name = "email_verified")
    private Boolean           emailVerified;

    @Column(name = "phone_number")
    private String            phoneNumber;

    @Type(type = "true_false")
    @Column(name = "phone_verified")
    private Boolean           phoneVerified;

    @Column(name = "date_of_birth")
    private LocalDate         dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private UserMaritalStatus maritalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus        status;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private UserGender        gender;

    @Lob
    @Column(name = "about_me")
    private String            aboutMe;

    @Column(name = "profile_image_url")
    private String            profileImageUrl;

    @Column(name = "cover_image_url")
    private String            coverImageUrl;

    @Column(name = "password_expiration_date")
    private LocalDate         passwordExpirationDate;

    @Column(name = "invalid_password_counter")
    private Integer           invalidPasswordCounter = 0;

    @JsonIgnoreProperties(value = {"users"})
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private Set<Role>         roles;

    @JoinColumn(name = "address_id", nullable = true)
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private Address           address;

    @Type(type = "true_false")
    @Column(name = "deleted", nullable = false)
    private boolean           deleted;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime     createdAt;

    @UpdateTimestamp
    @Column(name = "last_updated_at", nullable = false)
    private LocalDateTime     lastUpdatedAt;

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

    public User(long id) {
        this.id = id;
    }

    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
        this.roles.add(role);
    }

    public Set<String> generateStrRoles() {
        return roles.stream().map(role -> role.getAuthority().name()).collect(Collectors.toSet());
    }

    @PrePersist
    private void preCreate() {
        if (this.uuid == null || this.uuid.isEmpty()) {
            this.uuid = "user-" + UUID.randomUUID().toString();
        }

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
