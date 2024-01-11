package apiserver.apiserver.security;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Deprecated
@Service
public class ApiKeyGenerator {

	private static String apiKey;
	
	public ApiKeyGenerator() {
		
	}
	
	public String generateApiKey() {
		SecureRandom random = new SecureRandom();
		byte[] bytes = new byte[32];
		random.nextBytes(bytes);
		apiKey = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
		return apiKey;
	}
	
	public String getApiKey() {
		return apiKey;
	}
}
