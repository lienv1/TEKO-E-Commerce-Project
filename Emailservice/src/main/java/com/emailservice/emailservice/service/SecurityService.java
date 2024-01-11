package com.emailservice.emailservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Deprecated
@Service
public class SecurityService {
	
	private String apikey;
	private String preSharedKey;
	private String host;
	
	public SecurityService(@Value("${custom.property.presharedkey}") String preSharedKey, @Value("${custom.property.host}") String host, @Value("${custom.property.service.https.enabled}") boolean httpsEnabled ) {
		this.preSharedKey = preSharedKey;
		this.host = (httpsEnabled==true? "https://" : "http://") + host;
		setupAPIKey();
	}

	public boolean setupAPIKey() {
		try {
			RestTemplate restTemplate = new RestTemplate();

			String url = host + ":8080/config/apikey";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", preSharedKey);
			System.out.println("Pre shared key: " + preSharedKey);
			
			System.out.println(preSharedKey);
			
			HttpEntity<String> requestEntity = new HttpEntity<>(null,headers);
			
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
			if (response.getStatusCode().is2xxSuccessful()) {
				this.apikey = response.getBody();
				return true;
			} else
				return false;
		} catch (Exception e) {
			System.out.println("Failed ti setup APIKey");
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean apikeyIsEmpty() {
		return apikey == null;
	}
	
	public boolean apikeyIsValid(String apikey) {
		return this.apikey.equals(apikey);
	}
}
