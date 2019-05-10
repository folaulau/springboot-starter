package com.lovemesomecoding.dto;

import com.lovemesomecoding.address.Address;

public class UserDto {
	
	private String uid;

	private String name;

	private String email;

	private int age;
	
	private Address address;
	
	public UserDto() {
		this(null,null,null,0);
	}
	
	public UserDto(String uid, String name, String email, int age) {
		this(uid,name,email,age,null);
	}

	public UserDto(String uid, String name, String email, int age, Address address) {
		super();
		this.uid = uid;
		this.name = name;
		this.email = email;
		this.age = age;
		this.address = address;
	}



	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
