package com.emailservice.emailservice.service;

import org.springframework.stereotype.Service;

@Service
public class SecurityService {
	
	private String apikey;
	
	public SecurityService() {
		
	}
	
	public void setApiKey(String apikey) {
		this.apikey = apikey;
	}
	
	public boolean apikeyIsEmpty() {
		return apikey == null;
	}
	
	public boolean apikeyIsValid(String apikey) {
		return this.apikey.equals(apikey);
	}
}
