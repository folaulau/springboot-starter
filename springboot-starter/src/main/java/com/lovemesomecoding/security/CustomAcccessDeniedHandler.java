package com.lovemesomecoding.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.lovemesomecoding.utils.ObjectUtils;

@Component
public class CustomAcccessDeniedHandler implements AccessDeniedHandler {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.info("access denied");
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		
		ObjectNode result = ObjectUtils.getObjectNode();
		result.put("status", "failed");
		result.put("msg", "access denied");
		
		ObjectUtils.getObjectMapper().writeValue(response.getWriter(), result);
	}

}
