package com.lovemesomecoding.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.entity.user.UserGender;
import com.lovemesomecoding.utils.validator.Email;
import com.lovemesomecoding.utils.validator.Password;

import lombok.AllArgsConstructor;
import lombok.Data;
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
public class SignUpDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Email(message = "Invalid email")
    private String     email;

    @Password(message = "Invalid password")
    private String     password;

}
