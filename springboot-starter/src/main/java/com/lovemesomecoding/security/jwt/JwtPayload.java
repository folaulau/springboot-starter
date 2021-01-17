/*******************************************************************************
 * @ JwtPayload.java @ Project: SideCar Health Corporation
 *
 *   Copyright (c) 2018 SideCar Health Corporation. - All Rights Reserved El Segundo, California, U.S.A.
 *
 *   This software is the confidential and proprietary information of SideCar Health Corporation. ("Confidential
 *   Information").
 *
 *   You shall not disclose such Confidential Information and shall use it only in accordance with the terms of the
 *   license agreement you entered into with SideCar Corporation.
 *
 *   Contributors: SideCar Health Corporation. - Software Engineering Team
 ******************************************************************************/
package com.lovemesomecoding.security.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lovemesomecoding.utils.ObjMapperUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * https://tools.ietf.org/html/rfc7519 JWT Payload <br>
 * 1. sub - user uid <br>
 * 
 * @author folaukaveinga
 *
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class JwtPayload implements Serializable {

    private static final long serialVersionUID = -1L;

    // issuer
    private String            iss;

    // id of jwt
    private String            jti;

    // user uuid
    private String            sub;

    // issued at
    private Date              iat;

    // expired at
    private Date              exp;

    // not before
    private Date              nbf;

    // cud - user agent(where user is authenticated from)
    private String            cud;

    // aud - user roles
    private Set<String>       aud;

    public JwtPayload(String jti, String uuid, String cud, Set<String> aud) {
        this(null, jti, uuid, null, null, null, cud, aud);
    }

    public String[] getUserRolesAsArray() {
        int size = aud.size();
        String strRoles[] = new String[size];
        System.arraycopy(aud.toArray(), 0, strRoles, 0, size);
        return strRoles;
    }

    public String toJson() {
        try {
            return ObjMapperUtils.getObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println("JwtPayload - toJson - JsonProcessingException, msg: " + e.getLocalizedMessage());
            return "{}";
        }
    }

}
