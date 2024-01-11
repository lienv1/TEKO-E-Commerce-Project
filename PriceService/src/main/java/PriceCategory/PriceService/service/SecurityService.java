package PriceCategory.PriceService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

	String preSharedKey;
	
	public SecurityService(@Value("${custom.property.presharedkey}") String preSharedKey) {
		this.preSharedKey = preSharedKey;
	}
	
	public boolean validateKey(String preSharedKey) {
		return this.preSharedKey.equals(preSharedKey);
	}
	
}
