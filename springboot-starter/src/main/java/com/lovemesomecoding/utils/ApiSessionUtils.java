package com.lovemesomecoding.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.lovemesomecoding.jwt.JwtPayload;

public class ApiSessionUtils {

	public static JwtPayload getApiSession() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			JwtPayload session = (JwtPayload) auth.getPrincipal();
			if(session!=null) {
				return session;
			}
		}
		return null;
	}
	
	public static Long getApiSessionUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			JwtPayload session = (JwtPayload) auth.getPrincipal();
			
			if(session!=null && session.getId()!=null) {
				return session.getId();
			}
			
		}
		return null;
	}
	
	public static Long getCreateBy() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			JwtPayload session = (JwtPayload) auth.getPrincipal();
			
			if(session!=null && session.getId()!=null) {
				return session.getId();
			}
			
		}
		return new Long(1);
	}

	/**
	 * set jwtPayload in UsernamePasswordAuthenticationToken principal
	 * @param jwtPayload
	 */
	public static void setRequestSecurityAuthentication(JwtPayload jwtPayload) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		if (jwtPayload.getAuthorities() != null || jwtPayload.getAuthorities().isEmpty() == false) {
			for (String role : jwtPayload.getAuthorities()) {
				authorities.add(new SimpleGrantedAuthority(role.toUpperCase()));
			}
		}

		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(jwtPayload, jwtPayload.getUid(), authorities));
	}

}
