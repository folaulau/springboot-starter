package com.lovemesomecoding.security;

public class PathUtils {

public static final String LOGIN_URL = "/users/login";
	
	public static final String LOGOUT_URL ="/users/logout";
	
	public static final String SIGNUP_URL ="/users/signup";
	
	public static final String[] TEST_URLS = {
            "/tests/**",
			"/tests/generate-basic-auth-token",
            "/tests/system/session"
    };
	
	public static final String[] PUBLIC_URLS = {"/**"};
	
	public static final String[] SWAGGER_DOC_URLS = {
            "/swagger-ui.html",
            "/swagger-resources",
            "/swagger-resources/**",
            "/swagger-resources/configuration/ui",
            "/swagger-resources/configuration/security",
            "/v2/api-docs",
            "/webjars/**",
            "/webjars/springfox-swagger-ui/**",
            "/webjars/springfox-swagger-ui/springfox.css?v=2.8.0-SNAPSHOT",
            "/webjars/springfox-swagger-ui/swagger-ui.css?v=2.8.0-SNAPSHOT"
    };
}
