package com.emailservice.emailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emailservice.emailservice.model.Order;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/email")
public class EmailController {
	
	@Autowired
	private EmailService emailService;
	
	public EmailController(EmailService emailService) {
		this.emailService = emailService;
	}

	@PostMapping("/send/confirmation")
	public ResponseEntity<?> sendEmail(HttpServletRequest request, @RequestBody Order order) {
		String apikey = request.getHeader("X-API-KEY");
		if (!emailService.apikeyIsValid(apikey)) {
			return new ResponseEntity<String>("Invalid Api Key", HttpStatus.BAD_REQUEST);
		}
		
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/apikey/{apikey}")
	public ResponseEntity<?> receiveAPIKey(@PathVariable("apikey") String apikey){
		if (apikey == null || apikey == "") 
			return ResponseEntity.badRequest().build();
		if (!this.emailService.apikeyIsEmpty())
			return new ResponseEntity<String>("API Key already set", HttpStatus.BAD_REQUEST);
		this.emailService.setApiKey(apikey);
		return ResponseEntity.ok().build();
	}
	
	
}
