package apiserver.apiserver.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import apiserver.apiserver.model.Order;

@Service
public class ERPService {

	private String host;
	private String presharedKey;

	public ERPService(
			@Value("${custom.property.host}") String host,
			@Value("${custom.property.service.https.enabled}") 
			boolean httpsEnabled,
			@Value("${custom.property.presharedkey}") String preSharedKey) {
		this.host = (httpsEnabled == true ? "https://" : "http://") + host;
		this.presharedKey = preSharedKey;
	}

	public boolean sendOrderToERPServer(Order order) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
//		headers.set("X-API-KEY", configurationService.getApiKey());
		headers.set("Authorization", presharedKey);
		String url = host + ":8082/ftpservice/sendorder";

		ResponseEntity<String> response = restTemplate.postForEntity(url, new HttpEntity<>(order, headers),
				String.class);
		if (response.getStatusCode().is2xxSuccessful()) {
//			String responseBody = response.getBody();
			return true;
		} else
			return false;

	}

}
