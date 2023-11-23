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
	private ApiKeyGenerator apiKeyGenerator;

	
	public EmailService(ApiKeyGenerator apiKeyGenerator, @Value("${custom.property.host}") String host,
			@Value("${custom.property.presharedkey}") String preSharedKey) {
		this.apiKeyGenerator = apiKeyGenerator;
		this.host = "http://" + host;
		this.setupEmailAPIKey(preSharedKey);
	}

	public boolean sendingOrderConfirmation(Order order) {

		RestTemplate restTemplate = new RestTemplate();

		String url = host + ":8081/email/send/confirmation";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-API-KEY", apiKeyGenerator.getApiKey());
		ResponseEntity<String> response = restTemplate.postForEntity(url, order, String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
//			String responseBody = response.getBody();
			return true;
		} else
			return false;
	}

	public boolean setupEmailAPIKey(String preSharedKey) {
		try {
			RestTemplate restTemplate = new RestTemplate();

			String apikey = apiKeyGenerator.generateApiKey();

			String url = host + ":8081/email/apikey/" + apikey;

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", preSharedKey);
			System.out.println("Pre shared key: " + preSharedKey);
			System.out.println("api key: "+apikey);
			
			HttpEntity<String> requestEntity = new HttpEntity<>(headers);
			
			ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
			if (response.getStatusCode().is2xxSuccessful()) {
//			String responseBody = response.getBody();
				return true;
			} else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
