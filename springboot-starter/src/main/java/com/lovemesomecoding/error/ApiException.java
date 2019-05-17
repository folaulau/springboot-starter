/*******************************************************************************
 * @ ProcessException.java
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
 * ProcessException is an exception handler for processing resources such as POST, PUT, PATCH, DELETE
 * @author fkaveinga
 *
 */
@SuppressWarnings("serial")
public class ApiException extends RuntimeException {
	
	private ApiError error;

	public ApiException() {
		this(null);
	}

	public ApiException(ApiError error) {
		super(error.getMessage());
		this.error = error;
	}

	public ApiError getError() {
		return error;
	}

	public void setError(ApiError error) {
		this.error = error;
	}
}
