package com.lovemesomecoding.utils;

import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class HttpUtils {
	
	private static final String[] IP_HEADER_CANDIDATES = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };
	
	public static String generateBasicAuthenticationToken(String username, String password) {
		String auth = username + ":" + password;
		return "Basic " + new String( Base64.getEncoder().encodeToString(auth.getBytes()));
	}
	
	public static String getRequestIP(HttpServletRequest request) {
		String requestorIPAddress = "";

        if (request != null) {
        	requestorIPAddress = request.getHeader("X-FORWARDED-FOR");
            if (requestorIPAddress == null || "".equals(requestorIPAddress)) {
            	requestorIPAddress = request.getRemoteAddr();
            }
        }
        return requestorIPAddress;
	}
	
	public static String getRequestUserAgent(HttpServletRequest request) {
		String requestorUserAgent = "";
        if (request != null) {
        	requestorUserAgent = request.getHeader("user-agent");
            return (requestorUserAgent!=null && "".equals(requestorUserAgent)==false) ? requestorUserAgent: "";
        }
        return requestorUserAgent;
	}
	
	public static Map<String, Object> getRequestHeaderInfo(HttpServletRequest request) {
		Map<String, Object> obj = new HashMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			String headerValue = request.getHeader(headerName);

			obj.put("header_" + headerName, headerValue);
		}
		for (String headerName : IP_HEADER_CANDIDATES) {
			String headerValue = request.getHeader(headerName);
			obj.put("header_x_" + headerName, headerValue);

		}
		return obj;
	}

}
