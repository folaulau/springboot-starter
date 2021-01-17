package com.lovemesomecoding.security;

import lombok.extern.slf4j.Slf4j;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lovemesomecoding.cache.CacheService;
import com.lovemesomecoding.exception.ApiErrorResponse;
import com.lovemesomecoding.security.jwt.JwtPayload;
import com.lovemesomecoding.security.jwt.JwtTokenUtils;
import com.lovemesomecoding.utils.ApiSessionUtils;
import com.lovemesomecoding.utils.HttpUtils;
import com.lovemesomecoding.utils.ObjMapperUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.TimeZone;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
public class AuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * @author fkaveinga
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // log.debug("doFilterInternal(...)");

        String clientIpAddress = HttpUtils.getRequestIP(request);
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        String token = request.getHeader("token");
        log.debug("token: {}, ip address", token, clientIpAddress);
        log.debug("url: {}", HttpUtils.getFullURL(request));

        if (token == null || token.length() == 0) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(UNAUTHORIZED.value());

            String message = "Missing token in header";
            log.debug("Error message: {}, context path: {}, url: {}", message, request.getContextPath(), request.getRequestURI());

            ObjMapperUtils.getObjectMapper().writeValue(response.getWriter(), new ApiErrorResponse(UNAUTHORIZED, "Access Denied", Collections.singletonList(message)));

            return;
        }

        JwtPayload jwtPayload = JwtTokenUtils.getPayloadByToken(token);

        boolean authorized = authenticationService.authorizeRequest(token, jwtPayload);

        if (authorized == false) {
            return;
        }

        ThreadContext.put("userUuid", jwtPayload.getSub());

        // log.debug("token is valid");

        filterChain.doFilter(request, response);

        /*
         * Remove memberUuid from the logs as this thread is being terminated.
         */
        ThreadContext.clearMap();
    }

}
