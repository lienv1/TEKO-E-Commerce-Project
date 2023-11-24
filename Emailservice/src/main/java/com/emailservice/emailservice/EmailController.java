package com.emailservice.emailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emailservice.emailservice.model.Order;
import com.emailservice.emailservice.service.EmailService;
import com.emailservice.emailservice.service.SecurityService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/email")
public class EmailController {
	
	private String preSharedKey;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private SecurityService securityService;
	
	public EmailController(EmailService emailService, SecurityService securityService, @Value("${custom.property.presharedkey}") String preSharedKey) {
		this.emailService = emailService;
		this.securityService = securityService;
		this.preSharedKey = preSharedKey;
	}

	@PostMapping("/send/confirmation")
	public ResponseEntity<?> sendEmail(HttpServletRequest request, @RequestBody Order order) {
		String apikey = request.getHeader("X-API-KEY");
		String language = request.getHeader("lang");
		
		if (!securityService.apikeyIsValid(apikey)) 
			return new ResponseEntity<String>("Invalid Api Key", HttpStatus.BAD_REQUEST);
		this.emailService.sendConfirmation(order,language);
		return ResponseEntity.ok().build();
	}
	
//	@GetMapping("/testmail")
//	public ResponseEntity<?> testing(){
//		boolean success = emailService.sendTest();
//		return new ResponseEntity<Boolean>(success,HttpStatus.OK);
//	}
	
	
}
