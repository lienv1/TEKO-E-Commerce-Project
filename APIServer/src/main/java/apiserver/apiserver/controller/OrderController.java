package apiserver.apiserver.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apiserver.apiserver.exception.OrderNotFoundException;
import apiserver.apiserver.model.Order;
import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AuthorizationService authorizationService;
	
	public OrderController(OrderService orderService, AuthorizationService authorizationService) {
		this.orderService = orderService;
		this.authorizationService = authorizationService;
	}
	
	@PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
	@GetMapping("/id/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id, Principal principal) {
		
		boolean isAuthenticated = authorizationService.isAuthenticatedByPrincipal(principal, principal.getName());
		
		if (!isAuthenticated) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		
		try {
			Order order = orderService.getOrderById(id);
			return new ResponseEntity<Order>(order,HttpStatus.OK);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
		}
		
	}
	
}
