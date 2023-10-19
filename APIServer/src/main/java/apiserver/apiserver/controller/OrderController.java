package apiserver.apiserver.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apiserver.apiserver.exception.OrderNotFoundException;
import apiserver.apiserver.exception.UserNotFoundException;
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

	@GetMapping("/id/{id}")
	@PreAuthorize(value = "hasAnyRole('ADMIN','CUSTOMER')")
	public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id, Principal principal) {
		boolean isAuthenticated = authorizationService.isAuthenticatedByPrincipal(principal, principal.getName());

		if (!isAuthenticated) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		try {
			Order order = orderService.getOrderById(id);
			return new ResponseEntity<Order>(order, HttpStatus.OK);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/username/{username}")
	@PreAuthorize(value = "hasAnyRole('ADMIN','CUSTOMER')")
	public ResponseEntity<List<Order>> getOrderByUsername(@PathVariable("username") String username,
			Principal principal) {
		boolean isAuthenticated = authorizationService.isAuthenticatedByPrincipal(principal, username);

		if (!isAuthenticated) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		List<Order> orders = orderService.getAllOrdersByUsername(username);
		return new ResponseEntity<List<Order>>(orders, HttpStatus.OK);
	}

	@PostMapping("/username/{username}")
	@PreAuthorize(value = "hasAnyRole('ADMIN','CUSTOMER')")
	public ResponseEntity<Order> addOrder(@PathVariable("username") String username, @RequestBody Order order,
			Principal principal) {
		boolean isAuthenticated = authorizationService.isAuthenticatedByPrincipal(principal, username);
		if (!isAuthenticated) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		try {
			System.out.println("Trying to add order");
			Order addedOrder = orderService.addOrder(order);
			return new ResponseEntity<Order>(addedOrder, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

}
