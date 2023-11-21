package com.emailservice.emailservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.emailservice.emailservice.model.Order;

@Service
public class EmailService {

	private String apikey;
	
	@Autowired
	private JavaMailSender mailSender;
	
	public EmailService() {
		
	}
	
	public void setApiKey(String apikey) {
		this.apikey = apikey;
	}
	
	public boolean apikeyIsEmpty() {
		return apikey == null;
	}
	
	public boolean apikeyIsValid(String apikey) {
		return this.apikey.equals(apikey);
	}
	
	public boolean sendConfirmation(Order order) {

		String text = "";
		
		
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getUser().getEmail());
        message.setSubject("Confirmation mail");
        message.setText(text);
        mailSender.send(message);
		
		return false;
	}
}
