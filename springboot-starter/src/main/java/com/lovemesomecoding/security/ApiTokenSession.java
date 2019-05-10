package com.lovemesomecoding.security;

import java.io.Serializable;
import java.util.List;
import org.springframework.security.core.AuthenticatedPrincipal;

public class ApiTokenSession  implements Serializable, AuthenticatedPrincipal {

	private static final long serialVersionUID = -1L;

	// User Attributes
	private long userId;
	private String userUuid;
	private String userEmail;
	private List<String> userAuthorities;
	private String firstName;
	private String lastName;
	
	public ApiTokenSession() {
		super();
		// TODO Auto-generated constructor stub
	}



	public long getUserId() {
		return userId;
	}



	public void setUserId(long userId) {
		this.userId = userId;
	}



	public String getUserUuid() {
		return userUuid;
	}



	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}



	public String getUserEmail() {
		return userEmail;
	}



	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}



	public List<String> getUserAuthorities() {
		return userAuthorities;
	}



	public void setUserAuthorities(List<String> userAuthorities) {
		this.userAuthorities = userAuthorities;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return userEmail;
	}

}
