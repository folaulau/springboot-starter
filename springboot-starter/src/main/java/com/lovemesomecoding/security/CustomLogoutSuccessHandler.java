package com.lovemesomecoding.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.lovemesomecoding.dto.ApiDefaultResponseDTO;
import com.lovemesomecoding.utils.ObjMapperUtils;

/**
 * Handle log out success
 * 
 * @author fkaveinga
 *
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("Logout Success");
        ObjMapperUtils.getObjectMapper().writeValue(response.getWriter(), new ApiDefaultResponseDTO());
    }

}
