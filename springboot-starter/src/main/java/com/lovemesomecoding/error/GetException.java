/*******************************************************************************
 * @ GetException.java
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

/**
 * GetException is an exception handler for getting resources such as GET
 * @author fkaveinga
 *
 */
public class GetException extends RuntimeException {
	
	private ApiErrorResponse error;

	public GetException() {
		this(null);
	}

	public GetException(ApiErrorResponse error) {
		super(error.getMessage());
		this.error = error;
	}

	public ApiErrorResponse getError() {
		return error;
	}

	public void setError(ApiErrorResponse error) {
		this.error = error;
	}
}
