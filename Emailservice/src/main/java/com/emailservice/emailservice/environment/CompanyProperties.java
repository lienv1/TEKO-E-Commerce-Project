package com.emailservice.emailservice.environment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "custom.property")
public class CompanyProperties {

	private String company;
	private String street;
	private String zip;
	private String state;
	private String country;
	private String phone;
	private String mobile;
	private String email;
	private String web;
	
	public String getCompany() {
		return company;
	}
	public String getStreet() {
		return street;
	}
	public String getZip() {
		return zip;
	}
	public String getState() {
		return state;
	}
	public String getCountry() {
		return country;
	}
	public String getPhone() {
		return phone;
	}
	public String getMobile() {
		return mobile;
	}
	public String getEmail() {
		return email;
	}
	public String getWeb() {
		return web;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	
	
	
}
