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
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class ApiErrorResponse extends ApiResponse{
	
	public static final String DEFAULT_MSG = "Something went wrong. Please try again.";

	@JsonIgnore
	private HttpStatus status;
	
	@JsonIgnore
	private Date timestamp;
	
	//@JsonIgnore
	//private String debugMessage;

	//private List<ApiSubError> errors;
	
	private List<String> errors;
	
	public ApiErrorResponse() {
		this(null,null);
	}
	
	public ApiErrorResponse(String message) {
		this(null,message);
	}
	
	public ApiErrorResponse(HttpStatus status, String message) {
		this(status,message,null);
	}
	
	public ApiErrorResponse(HttpStatus status, String message, List<String> subErrors) {
		this(status,message,null,subErrors);
	}
	
	public ApiErrorResponse(HttpStatus status, String message, String debugMessage, List<String> subErrors) {
		this(status,message,debugMessage,subErrors,null);
	}
	
	public ApiErrorResponse(HttpStatus status, String message,  String debugMessage, List<String> subErrors, Throwable ex) {
		this.status = status;
		this.message = message;
		this.timestamp = new Date();
		this.errors = subErrors;
		//this.debugMessage = (debugMessage!=null) ? debugMessage : (ex!=null) ? ex.getLocalizedMessage() : null;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

//	public String getDebugMessage() {
//		return debugMessage;
//	}
//
//	public void setDebugMessage(String debugMessage) {
//		this.debugMessage = debugMessage;
//	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> subErrors) {
		this.errors = subErrors;
	}
	
	public void addSubError(String subError) {
		if(this.errors==null){
			this.errors = new ArrayList<>();
		}
		this.errors.add(subError);
	}
}
