package com.emailservice.emailservice.model;

public class Address {
	
    private Long addressId;
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    
    public Address() {
    	
    }
    
	public Long getAddressId() {
		return addressId;
	}
	public String getStreet() {
		return street;
	}
	public String getCity() {
		return city;
	}
	public String getState() {
		return state;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public String getCountry() {
		return country;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public void setCountry(String country) {
		this.country = country;
	}
    
    

}
