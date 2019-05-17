package com.lovemesomecoding.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.lovemesomecoding.security.CustomAcccessDeniedHandler;
import com.lovemesomecoding.security.CustomAuthenticationFilter;
import com.lovemesomecoding.security.CustomAuthenticationProvider;
import com.lovemesomecoding.security.CustomLoginFilter;
import com.lovemesomecoding.security.CustomLogoutHandler;
import com.lovemesomecoding.security.CustomLogoutSuccessHandler;
import com.lovemesomecoding.security.PathUtils;


//
//
//
/**
 * To enable Authentication Filter do the following <br>
 * 1. Wire bean of CustomAuthenticationFilter into this class <br>
 * 2. Add bean as the authentication filter
 * 
 * @author folaukaveinga
 * 
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Autowired
	private CustomLogoutHandler customLogoutHandler;
	
	@Autowired
	private CustomLogoutSuccessHandler customLogoutSuccessHandler;
	
	@Autowired
	private CustomAcccessDeniedHandler customAcccessDeniedHandler;
	
	@Bean
	public CustomLoginFilter customUsernamePassworAuthenticationFilter() throws Exception {
		return new CustomLoginFilter(PathUtils.LOGIN_URL,authenticationManagerBean());
	}
	
	@Bean
	public CustomAuthenticationFilter customAuthenticationFilter() {
		return new CustomAuthenticationFilter();
	}
	
	@Bean
	public RegistrationBean jwtAuthFilterRegister(CustomAuthenticationFilter customAuthenticationFilter) {
		FilterRegistrationBean<CustomAuthenticationFilter> registrationBean = new FilterRegistrationBean<CustomAuthenticationFilter>(
				customAuthenticationFilter);
		registrationBean.setEnabled(false);
		return registrationBean;
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// rest call rules
		http
			.cors().and().csrf().disable()
			.authorizeRequests()
				.antMatchers(PathUtils.SIGNUP_URL).permitAll()
				.antMatchers(PathUtils.LOGIN_URL).permitAll()
				.antMatchers(PathUtils.TEST_URLS).permitAll()
				.anyRequest().permitAll();
					
		// logout
		http.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher(PathUtils.LOGOUT_URL))
			.addLogoutHandler(customLogoutHandler)
			.logoutSuccessHandler(customLogoutSuccessHandler);
		
		// filter
		http.addFilterBefore(customUsernamePassworAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
		
		http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		
		// stateless
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		// handler access denied calls
		http.exceptionHandling().accessDeniedHandler(customAcccessDeniedHandler);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.authenticationProvider(customAuthenticationProvider);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers(PathUtils.SIGNUP_URL)
			.antMatchers(PathUtils.TEST_URLS)
			.antMatchers("/actuator/**");
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
	@Bean
	public MethodInvokingFactoryBean methodInvokingFactoryBean() {
	    MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
	    methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
	    methodInvokingFactoryBean.setTargetMethod("setStrategyName");
	    methodInvokingFactoryBean.setArguments(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
	    return methodInvokingFactoryBean;
	}
}