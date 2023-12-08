package apiserver.apiserver.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apiserver.apiserver.exception.OrderNotFoundException;
import apiserver.apiserver.model.Order;
import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.EmailService;
import apiserver.apiserver.service.ERPService;
import apiserver.apiserver.service.OrderService;
import apiserver.apiserver.service.UserService;

@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ERPService erpService;
	
	public OrderController(OrderService orderService, AuthorizationService authorizationService, UserService userService, EmailService emailService, ERPService erpService) {
		this.orderService = orderService;
		this.authorizationService = authorizationService;
		this.userService = userService;
		this.emailService = emailService;
		this.erpService = erpService;
	}

	@GetMapping("/id/{id}")
	@PreAuthorize(value = "hasAnyRole('ADMIN','CUSTOMER')")
	public ResponseEntity<Order> getOrderById(@PathVariable("id") Long id, Principal principal) {
		try {
			Order order = orderService.getOrderById(id);
			boolean isAuthenticated = authorizationService.isAuthenticatedByPrincipal(principal, order.getUser().getUsername());
			if (!isAuthenticated)
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<Order>(order, HttpStatus.OK);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/username/{username}")
	@PreAuthorize("hasRole('ADMIN') or #username ==  authentication.name")
	public ResponseEntity<Page<Order>> getOrdersByUsername(@PathVariable("username") String username,
			Principal principal,
			Pageable page
			) {
		boolean userExist = userService.userExistsByUsername(username);
		if (!userExist) return ResponseEntity.notFound().build();		
		
		Page<Order> orders = orderService.getAllOrdersByUsername(username,page);
		return new ResponseEntity<Page<Order>>(orders, HttpStatus.OK);
	}
	
	@GetMapping("/userid/{userid}")
	@PreAuthorize(value = "hasRole('ADMIN')")
	public ResponseEntity<Page<Order>> getOrdersByUserId(@PathVariable("userid") int userid,
			Principal principal,
			Pageable page
			) {
		boolean userExist = userService.userExistByUserId(userid);
		if (!userExist) return ResponseEntity.notFound().build();		

		Page<Order> orders = orderService.getAllOrdersByUserid(userid,page);
		return new ResponseEntity<Page<Order>>(orders, HttpStatus.OK);
	}

	@PostMapping("/username/{username}")
	@PreAuthorize("hasRole('ADMIN') or #username ==  authentication.name")
	public ResponseEntity<Order> addOrder(
			@PathVariable("username") String username, 
			@RequestBody Order order, 
			@RequestParam(value="lang", required = false) String language,
			Principal principal) {	
		
		boolean userExist = userService.userExistsByUsername(username);
		if (!userExist) return ResponseEntity.notFound().build();		
		
		try {
			System.out.println("Trying to add order");
			Order addedOrder = orderService.addOrder(order);
//			System.out.println("Sending email...");
//			boolean emailSuccess = emailService.sendingOrderConfirmation(addedOrder,language);
//			System.out.println("Email Success: " + emailSuccess);
			boolean erpSuccess = erpService.sendOrderToERPServer(addedOrder);
			System.out.println("ERP success " + erpSuccess);
			
			return new ResponseEntity<Order>(addedOrder, HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

}
