package apiserver.apiserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apiserver.apiserver.security.ApiKeyGenerator;

@Service
public class ConfigurationService {
	
	@Autowired
	private ApiKeyGenerator apiKeyGenerator;
	
	public ConfigurationService(ApiKeyGenerator apiKeyGenerator) {
		this.apiKeyGenerator = apiKeyGenerator;
		this.apiKeyGenerator.generateApiKey();
	}
	
	public String getApiKey() {
		return apiKeyGenerator.getApiKey();
	}
	
	

}
