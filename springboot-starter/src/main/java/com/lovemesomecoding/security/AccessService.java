package com.lovemesomecoding.security;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lovemesomecoding.role.Role;

@Service
public class AccessService {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public boolean isAdmin() {
		log.info("isAdmin()");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && auth.isAuthenticated()
				&& auth.getAuthorities().stream()
						.anyMatch(role -> Arrays
								.asList(Role.ADMIN)
								.contains(role.getAuthority().trim()));
	}
}
