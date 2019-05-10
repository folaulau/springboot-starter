/*******************************************************************************
 * @ ApiSuccessResponse.java
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

@JsonInclude(value=Include.NON_NULL)
public class ApiSuccessResponse extends ApiResponse {

	private String status;
	private String token;
	
	public ApiSuccessResponse() {
		this(null);
	}
	
	public ApiSuccessResponse(String status) {
		this(status,null);
	}
	
	public ApiSuccessResponse(String status, String message) {
		this(status,message,null);
	}
	
	public ApiSuccessResponse(String status,  String message, String token) {
		super(message);
		this.status = status;
		this.token = token;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "ApiSuccessResponse [status=" + status + ", token=" + token + "]";
	}
}
