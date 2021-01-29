package com.lovemesomecoding.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.entity.user.UserGender;
import com.lovemesomecoding.entity.user.UserMaritalStatus;
import com.lovemesomecoding.entity.user.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(value = Include.NON_NULL)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID       = 1L;

    private Long              id;

    private String            uuid;

    private String            firstName;

    private String            middleName;

    private String            lastName;

    private String            email;

    private Boolean           emailVerified;

    private String            phoneNumber;

    private Boolean           phoneVerified;

    private LocalDate         dateOfBirth;

    private UserStatus        status;

    private UserGender        gender;

    private String            profileImageUrl;

    private String            coverImageUrl;

    private LocalDate         passwordExpirationDate;

    private Integer           invalidPasswordCounter = 0;

    private AddressDTO        address;

    private String            aboutMe;

    private boolean           deleted;

    private LocalDateTime     createdAt;

    private LocalDateTime     lastUpdatedAt;

}
