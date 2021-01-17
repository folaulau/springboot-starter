package com.lovemesomecoding.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.GrantedAuthority;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.lovemesomecoding.security.jwt.JwtPayload;

public interface ApiSessionUtils {

    static Logger log = LoggerFactory.getLogger(ApiSessionUtils.class);

    // /**
    // * save sidecarBrokerApiSession as principal for duration of rest call
    // *
    // * @param sidecarBrokerApiSession
    // */
    public static void setSessionToken(WebAuthenticationDetails authDetails, JwtPayload jwtPayload) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (jwtPayload.getAud() != null || jwtPayload.getAud().isEmpty() == false) {
            for (String role : jwtPayload.getAud()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
            }
        }

        UsernamePasswordAuthenticationToken updateAuth = new UsernamePasswordAuthenticationToken(jwtPayload, jwtPayload.getSub(), authorities);

        updateAuth.setDetails(authDetails);

        SecurityContextHolder.getContext().setAuthentication(updateAuth);
    }

    public static JwtPayload getApiSession() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            try {
                JwtPayload session = (JwtPayload) auth.getPrincipal();
                return session;
            } catch (Exception e) {
                log.warn("Exception, msg={}", e.getLocalizedMessage());
            }

        }
        return null;
    }

    public static String getUserUuid() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            try {
                JwtPayload session = (JwtPayload) auth.getPrincipal();
                return session.getSub();
            } catch (Exception e) {
                log.warn("Exception, msg={}", e.getLocalizedMessage());
            }

        }
        return null;
    }

    public static String getActiveProfile(Environment environment) {
        String env = "";
        try {
            env = Arrays.asList(environment.getActiveProfiles()).get(0);
        } catch (Exception e) {
        }
        return env;
    }

}
