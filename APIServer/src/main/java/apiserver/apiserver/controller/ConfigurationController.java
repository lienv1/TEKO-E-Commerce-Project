package apiserver.apiserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apiserver.apiserver.service.ConfigurationService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/config")
public class ConfigurationController {

	@Autowired
	private ConfigurationService configurationServce;
	
	private final String preSharedKey;
	
	public ConfigurationController(ConfigurationService configurationServce, @Value("${custom.property.presharedkey}") String preSharedKey) {
		this.configurationServce = configurationServce;
		this.preSharedKey = preSharedKey;
	}
	
	@GetMapping("/apikey")
	public ResponseEntity<String> getApiKey(HttpServletRequest request){
		String preSharedKey = request.getHeader("Authorization");
		if (!preSharedKey.equals(this.preSharedKey)) 
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		String apikey = configurationServce.getApiKey();
		return new ResponseEntity<String>(apikey,HttpStatus.OK);
	}
	
	
}
