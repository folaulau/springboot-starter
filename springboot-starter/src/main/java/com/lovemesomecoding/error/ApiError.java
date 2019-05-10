/*******************************************************************************
 * @ ApiErrorResponse.java
 * @ Project: SideCar Health Corporation
 *
 * Copyright (c) 2018 SideCar Health Corporation. - All Rights Reserved
 * El Segundo, California, U.S.A.
 *
 * This software is the confidential and proprietary information of
 * SideCar Health Corporation. ("Confidential Information").
 *
 * You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement
 * you entered into with SideCar Corporation.
 *
 * Contributors:
 * SideCar Health Corporation. - Software Engineering Team
 ******************************************************************************/
package com.lovemesomecoding.error;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lovemesomecoding.utils.DateTimeUtils;

/**
 * Reference - https://www.toptal.com/java/spring-boot-rest-api-error-handling <br>
 * @author folaukaveinga
 *
 */

@JsonInclude(value=Include.NON_NULL)
public class ApiError{

	private HttpStatus status;
	
	private String timestamp;
	
	// message that ui displays to user
	private String message;
	
	// message for developers
	private String debugMessage;
	
	// errors on forms
	private List<ApiSubError> errors;
	
	public ApiError() {
		this(null,null);
	}
	
	public ApiError(String message) {
		this(null,message);
	}
	
	public ApiError(HttpStatus status, String message) {
		this(status,message,null);
	}
	
	public ApiError(HttpStatus status, String message, String debugMessage) {
		this(status,message,debugMessage,null);
	}
	
	public ApiError(HttpStatus status, String message, String debugMessage, List<ApiSubError> subErrors) {
		this.status = (status==null) ? HttpStatus.BAD_REQUEST : status;
		this.message = message;
		this.timestamp = DateTimeUtils.getFormattedCurrentDateTime();
		this.errors = subErrors;
		this.debugMessage = debugMessage;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDebugMessage() {
		return debugMessage;
	}

	public void setDebugMessage(String debugMessage) {
		this.debugMessage = debugMessage;
	}

	public List<ApiSubError> getErrors() {
		return errors;
	}

	public void setErrors(List<ApiSubError> subErrors) {
		this.errors = subErrors;
	}
	
	public void addSubError(ApiSubError subError) {
		if(this.errors==null){
			this.errors = new ArrayList<>();
		}
		this.errors.add(subError);
	}
}
