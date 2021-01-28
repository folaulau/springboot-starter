package com.lovemesomecoding.enitity.user.session;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
@DynamicUpdate
@Entity
@Table(name = "user_session", indexes = {@Index(columnList = "user_id"), @Index(columnList = "user_uuid")})
public class UserSession implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long              id;

    @Column(name = "user_uuid", nullable = false, updatable = false)
    private String            userUuid;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long              userId;

    @Lob
    @Column(name = "auth_token", nullable = false, updatable = false, length = 2000, columnDefinition = "BLOB")
    private String            authToken;

    @Column(name = "user_agent", nullable = false, updatable = false, length = 1000)
    private String            userAgent;

    @Column(name = "device_app_name")
    private String            deviceAppName;

    @Column(name = "device_app_version")
    private String            deviceAppVersion;

    @Column(name = "device_OS_name")
    private String            deviceOSName;

    @Column(name = "device_OS_version")
    private String            deviceOSVersion;

    @Column(name = "ip_address")
    private String            ipAddress;

    // ======================location=======================
    @Column(name = "country")
    private String            country;

    @Column(name = "street")
    private String            street;

    @Column(name = "street2")
    private String            street2;

    @Column(name = "city")
    private String            city;

    // state or region
    @Column(name = "state")
    private String            state;

    @Column(name = "county")
    private String            county;

    @Column(name = "zipcode")
    private String            zipcode;

    @Column(name = "timezone")
    private String            timezone;

    @Column(name = "lat")
    private Double            lat;

    @Column(name = "lng")
    private Double            lng;
    // ======================*location*=======================

    @Column(name = "log_in_time", nullable = false, updatable = false)
    private Date              loginTime;

    @Column(name = "log_out_time", nullable = true, updatable = true)
    private Date              logoutTime;

    @Column(name = "time_expired", nullable = true, updatable = true)
    private Date              expired;

    @Type(type = "true_false")
    @Column(name = "active", nullable = false, updatable = true)
    private Boolean           active;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.id).append(this.authToken).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        UserSession other = (UserSession) obj;
        return new EqualsBuilder().append(this.id, other.id).append(this.authToken, other.authToken).isEquals();
    }

}
