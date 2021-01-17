package com.lovemesomecoding.dto.helper;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * This object is store in redis
 * 
 * @author folaukaveinga
 */
@JsonInclude(value = Include.NON_NULL)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ApiSession implements Serializable {

    private static final long serialVersionUID = 1L;

    // In case user needs to invalidate token
    private String            token;

    private long              userId;

    private String            userUuid;

    private Set<String>       userRoles;

    // last time token was used
    private Date              lastUsedTime;

    // expiration time
    private Date              expiredTime;

    // userAgent
    private String            deviceId;

    private String            clientIPAddress;

    public ApiSession extendLifeTimeOnRequest() {
        // expire next 24 hours
        this.setExpiredTime(DateUtils.addHours(new Date(), 24));
        return this;
    }

}
