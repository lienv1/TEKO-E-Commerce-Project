package com.emailservice.emailservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.emailservice.emailservice.model.Order;

@Service
public class EmailService {

	
	
	@Autowired
	private JavaMailSender mailSender;
	
	public EmailService() {
		
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
