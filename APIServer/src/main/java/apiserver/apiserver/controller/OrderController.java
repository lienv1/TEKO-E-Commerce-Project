package apiserver.apiserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.OrderService;

@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AuthorizationService authorizationService;
	
	public OrderController(OrderService orderService, AuthorizationService authorizationService) {
		this.orderService = orderService;
		this.authorizationService = authorizationService;
	}
	
	
	
}
