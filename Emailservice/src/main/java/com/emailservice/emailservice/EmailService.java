package com.emailservice.emailservice;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private String apikey;
	
	public EmailService() {
		
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
