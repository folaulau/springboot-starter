/*******************************************************************************
 * @ ApiSubError.java
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Reference - https://www.toptal.com/java/spring-boot-rest-api-error-handling<br>
 * @author folaukaveinga
 *
 */
@JsonInclude(value=Include.NON_NULL)
public class ApiSubError {
	
	// name of the object or entity
	private String object;
	
	// name of the field
	private String field;
	
	// value that is rejected
	private Object rejectedValue;
	
	// error message to display
	private String message;

	public ApiSubError(String object, String message) {
		this(object, message, null );
	}
	
	public ApiSubError(String object, String message, String field) {
		this(object, message, field, null);
	}

	public ApiSubError(String object, String message, String field, Object rejectedValue) {
		super();
		this.object = object;
		this.field = field;
		this.rejectedValue = rejectedValue;
		this.message = message;
	}



	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public Object getRejectedValue() {
		return rejectedValue;
	}

	public void setRejectedValue(Object rejectedValue) {
		this.rejectedValue = rejectedValue;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ApiSubError [object=" + object + ", field=" + field + ", rejectedValue=" + rejectedValue + ", message="
				+ message + "]";
	}
}
