package com.lovemesomecoding.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.lovemesomecoding.role.Role;
import com.lovemesomecoding.user.User;
import com.lovemesomecoding.user.UserService;
import com.lovemesomecoding.utils.ObjectUtils;
import com.lovemesomecoding.utils.PasswordUtils;

/**
 * CustomAuthenticationProvider
 * 
 * @author fkaveinga
 *
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;

	/**
	 * Authenticate user by credentials
	 * 
	 * @author fkaveinga
	 * @return Authentication
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("authenticate(...)");
		
		String email = authentication.getPrincipal().toString();
		String password = authentication.getCredentials().toString();
		
		log.info("email: {}", email);
		log.info("password: {}", password);
		
		Map<String, String> details = (Map) authentication.getDetails();
		log.debug("details: {}", ObjectUtils.toJson(details));
		
		return loginWithPassword(email, password);
	}

	private Authentication loginWithPassword(String email, String password) {
		log.info("loginWithPassword({})", email);
		Optional<User> optUser = userService.findByEmail(email);

		if (!optUser.isPresent()) {
			log.info("user not found");
			throw new UsernameNotFoundException("Username or password is invalid");
		}
		
		log.info("user found for {}", email);
		
		User user = optUser.get();
		
		log.info("user: {}", ObjectUtils.toJson(user));
		
		if (user.getPassword() == null || !PasswordUtils.verify(password, user.getPassword())) {
			log.info("login credentials not matched");
			throw new BadCredentialsException("Username or password is invalid");
		}

		return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), generateAuthorities(user.getRoles()));
	}
	

	/**
	 * Get Authorities for User
	 * 
	 * @param user
	 * @return List<GrantedAuthority>
	 */
	private List<GrantedAuthority> generateAuthorities(Set<Role> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		if (roles.isEmpty()) {
			throw new InsufficientAuthenticationException("No role");
		} else {
			for (Role role : roles) {
				authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getAuthority().toUpperCase()));
			}
		}
		return authorities;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
