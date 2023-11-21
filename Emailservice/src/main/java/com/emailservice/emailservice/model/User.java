package com.emailservice.emailservice.model;

public class User {

	private Long userId;

	private String company;
	private String firstname;
	private String lastname;
	private String phone;
	private String email;

	public User() {

	}

	public Long getUserId() {
		return userId;
	}

	public String getCompany() {
		return company;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
