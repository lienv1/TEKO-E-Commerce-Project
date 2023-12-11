package apiserver.apiserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import apiserver.apiserver.model.Order;
import apiserver.apiserver.security.ApiKeyGenerator;

@Service
public class EmailService {

	private String host;

	@Autowired
	private ConfigurationService configurationService;
	
	public EmailService(ConfigurationService configurationService, @Value("${custom.property.host}") String host,
			  @Value("${custom.property.service.https.enabled}") boolean httpsEnabled) {
		this.configurationService = configurationService;
		this.host = httpsEnabled==true? "https://" + host : "http://" + host;
		System.out.println("email service "+this.host);
	}

	public boolean sendingOrderConfirmation(Order order, String language) {

		RestTemplate restTemplate = new RestTemplate();

		String url = host + ":8081/email/send/confirmation";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-API-KEY", configurationService.getApiKey());
		headers.set("lang", language == null ? "EN" : language);
		ResponseEntity<String> response = restTemplate.postForEntity(url, new HttpEntity<>(order,headers), String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
//			String responseBody = response.getBody();
			return true;
		} else
			return false;
	}

}
