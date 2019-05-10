package com.lovemesomecoding.config;

import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.RegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.NullSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import com.lovemesomecoding.security.CustomAuthenticationFilter;
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
		super.configure(http);
		http.cors().and().csrf().disable()
		.authorizeRequests()
		.antMatchers(PathUtils.LOGIN_URL).permitAll()
		.anyRequest()
				.permitAll();

		http.addFilterBefore(securityContextPersistenceFilter(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// handler access denied calls
		// http.exceptionHandling().accessDeniedHandler(customAcccessDeniedHandler);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers(PathUtils.PUBLIC_URLS)
			.antMatchers(PathUtils.SWAGGER_DOC_URLS)
			.antMatchers("/actuator/**");
	}
	
	@Bean
	public MethodInvokingFactoryBean methodInvokingFactoryBean() {
		MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
		methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
		methodInvokingFactoryBean.setTargetMethod("setStrategyName");
		methodInvokingFactoryBean.setArguments(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
		return methodInvokingFactoryBean;
	}
	
	@Bean
	public SecurityContextPersistenceFilter securityContextPersistenceFilter() {
		SecurityContextPersistenceFilter securityContextPersistenceFilter = new SecurityContextPersistenceFilter(
				new NullSecurityContextRepository());
		return securityContextPersistenceFilter;
	}
}
