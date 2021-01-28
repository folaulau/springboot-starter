package com.lovemesomecoding.security;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import com.lovemesomecoding.cache.CacheService;
import com.lovemesomecoding.dto.AuthenticationResponseDTO;
import com.lovemesomecoding.dto.EntityDTOMapper;
import com.lovemesomecoding.dto.helper.ApiSession;
import com.lovemesomecoding.entity.user.User;
import com.lovemesomecoding.entity.user.session.UserSession;
import com.lovemesomecoding.entity.user.session.UserSessionService;
import com.lovemesomecoding.exception.ApiErrorResponse;
import com.lovemesomecoding.exception.ApiException;
import com.lovemesomecoding.security.jwt.JwtPayload;
import com.lovemesomecoding.security.jwt.JwtTokenService;
import com.lovemesomecoding.utils.ApiSessionUtils;
import com.lovemesomecoding.utils.HttpUtils;
import com.lovemesomecoding.utils.ObjMapperUtils;
import com.lovemesomecoding.utils.RandomGeneratorUtils;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class AuthenticationServiceImp implements AuthenticationService {

    @Autowired
    private HttpServletRequest  request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private EntityDTOMapper     entityMapper;

    @Autowired
    private CacheService        cacheService;

    @Autowired
    private UserSessionService  userSessionService;
    
    @Autowired
    private JwtTokenService       jwtTokenService;

    @Override
    public AuthenticationResponseDTO authenticate(User user) {
        log.debug("authenticate, user={}", ObjMapperUtils.toJson(user));
        String userAgent = HttpUtils.getRequestUserAgent(request);
        String userIPAddress = HttpUtils.getRequestIP(request);

        String jwtToken = jwtTokenService.generateToken(new JwtPayload(RandomGeneratorUtils.getJWTId(), user.getUuid()));

        AuthenticationResponseDTO authenticatedSessionDTO = entityMapper.mapUserToUserAuthSuccessDTO(user);
        authenticatedSessionDTO.setToken(jwtToken);

        ApiSession apiSession = new ApiSession();
        apiSession.setToken(jwtToken);
        apiSession.setUserId(user.getId());
        apiSession.setUserUuid(user.getUuid());
        apiSession.setUserRoles(user.generateStrRoles());
        apiSession.setClientIPAddress(userIPAddress);
        apiSession.setLastUsedTime(new Date());

        // next 24 hours
        apiSession.setExpiredTime(DateUtils.addHours(new Date(), 24));
        apiSession.setDeviceId(userAgent);

        cacheService.addUpdate(jwtToken, apiSession);

        UserSession userSession = new UserSession();
        userSession.setUserId(user.getId());
        userSession.setUserUuid(user.getUuid());
        userSession.setAuthToken(jwtToken);
        userSession.setLoginTime(new Date());
        userSession.setUserAgent(userAgent);

        userSessionService.signIn(userSession);

        return authenticatedSessionDTO;
    }

    @Override
    public boolean logOutUser(String token) {
        log.debug("logOutUser, token={}", token);
        ApiSession apiSession = cacheService.getApiSessionToken(token);

        if (apiSession != null) {
            long deleteCount = cacheService.delete(token);

            log.debug("deleteCount={}", deleteCount);

            boolean signOutInDB = userSessionService.signOut(token);

            log.debug("signOutInDB={}", signOutInDB);
            
            return deleteCount > 0;
        } else {
            return false;
        }

    }

    @Override
    public boolean authorizeRequest(String token, JwtPayload jwtPayload) {

        log.debug("jwtPayload={}", ObjMapperUtils.toJson(jwtPayload));

        if (jwtPayload == null) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(UNAUTHORIZED.value());

            String message = "Invalid token in header";
            log.debug("Error message: {}, context path: {}, url: {}", message, request.getContextPath(), request.getRequestURI());

            try {
                ObjMapperUtils.getObjectMapper().writeValue(response.getWriter(), new ApiErrorResponse(UNAUTHORIZED, "Access Denied", message, "Unable to verify token"));
            } catch (IOException e) {
                log.warn("IOException, msg={}", e.getLocalizedMessage());
            }

            return false;
        }

        ApiSession apiSession = cacheService.getApiSessionToken(token);

        if (apiSession == null) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(UNAUTHORIZED.value());

            String message = "Invalid token";
            log.debug("Token does not exist in cache. Error message: {}, context path: {}, url: {}", message, request.getContextPath(), request.getRequestURI());

            try {
                ObjMapperUtils.getObjectMapper().writeValue(response.getWriter(), new ApiErrorResponse(UNAUTHORIZED, "Access Denied", message, "Session not found in cache for requested token"));
            } catch (IOException e) {
                log.warn("IOException, msg={}", e.getLocalizedMessage());
            }

            return false;
        }

        String userAgent = HttpUtils.getRequestUserAgent(request);

        if (apiSession.getDeviceId() != null && apiSession.getDeviceId().equals(userAgent) == false) {
            log.warn("Device id or user-agent does not match the request user-agent");
        }

        log.debug("expiredTime={}, now={}", apiSession.getExpiredTime().toInstant().toString(), new Date().toInstant().toString());

        // is now after the expiration time?
        if (new Date().after(apiSession.getExpiredTime())) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(UNAUTHORIZED.value());

            String message = "Expired token";
            log.debug("Session expired. Error message: {}, context path: {}, url: {}", message, request.getContextPath(), request.getRequestURI());

            try {
                ObjMapperUtils.getObjectMapper()
                        .writeValue(response.getWriter(),
                                new ApiErrorResponse(UNAUTHORIZED, "Access Denied", message, "Token has been expired. expired at " + apiSession.getExpiredTime().toInstant().toString()));
            } catch (IOException e) {
                log.warn("IOException, msg={}", e.getLocalizedMessage());
            }

            return false;
        }

        /*
         * Valid token
         */
        apiSession = apiSession.extendLifeTimeOnRequest();
        cacheService.addUpdate(token, apiSession);

        ApiSessionUtils.setSessionToken(new WebAuthenticationDetailsSource().buildDetails(request), apiSession);

        return true;
    }

}
