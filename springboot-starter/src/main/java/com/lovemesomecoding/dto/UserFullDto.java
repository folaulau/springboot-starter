package com.lovemesomecoding.dto;

import java.io.Serializable;

public class UserFullDto  implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;

	private String email;

	private int age;
	
	private String state;

	private String zip;

	public UserFullDto() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZipcode(String zip) {
		this.zip = zip;
	}
}
