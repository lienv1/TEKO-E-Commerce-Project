package apiserver.apiserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import apiserver.apiserver.dto.PriceCategoryDTO;
import apiserver.apiserver.dto.ProductDTO;
import apiserver.apiserver.model.Product;

@Service
public class PriceService {

	private String host;
	private String presharedKey;

	public PriceService(@Value("${custom.property.host}") String host,
			@Value("${custom.property.service.https.enabled}") boolean httpsEnabled,
			@Value("${custom.property.presharedkey}") String preSharedKey) {
		this.host = (httpsEnabled == true ? "https://" : "http://") + host;
		this.presharedKey = preSharedKey;
	}

	public Double getPrice(Long erpId, Product product, int quantity) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", presharedKey);
		String url = host + ":8083/pricecategory/quantity/" + quantity + "/customer/" + erpId;

		try {
			ResponseEntity<Double> response = restTemplate.postForEntity(url, new HttpEntity<>(product, headers),
					Double.class);
			if (response.getStatusCode().is2xxSuccessful()) {
//			String responseBody = response.getBody();
				return response.getBody();
			} else
				return null;
		} catch (RestClientException e) {
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<PriceCategoryDTO> getPrices(Long erpId, List<ProductDTO> products) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", presharedKey);
		String url = host + ":8083/pricecategory/customer/" + erpId;

		ResponseEntity<List<PriceCategoryDTO>> response = restTemplate.exchange(url, HttpMethod.POST,
				new HttpEntity<>(products, headers), new ParameterizedTypeReference<List<PriceCategoryDTO>>() {
				});

		if (response.getStatusCode().is2xxSuccessful()) {
//			String responseBody = response.getBody();
			return response.getBody();
		} else
			return null;
	}

}
