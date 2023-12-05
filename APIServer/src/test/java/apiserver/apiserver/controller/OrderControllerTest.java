package apiserver.apiserver.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import apiserver.apiserver.exception.OrderNotFoundException;
import apiserver.apiserver.exception.UserNotFoundException;
import apiserver.apiserver.model.Order;
import apiserver.apiserver.model.OrderDetail;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.model.User;
import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.EmailService;
import apiserver.apiserver.service.OrderService;
import apiserver.apiserver.service.UserService;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

	@MockBean
	private OrderService orderService;
	
	@MockBean
	private AuthorizationService authorizationService;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private EmailService emailservice;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private User user;
	private Product product;
	private Order order;
	private Pageable pageable;
	
	@BeforeEach
	void init() {
		user = new User();
		user.setUsername("johndoe");
		user.setUserId(1000l);	
		
		product = new Product();
		product.setProductId(14326l);
		product.setProductName("Sriracha Hot Chili Sauce");
	
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setQuantity(2);
		
		order = new Order();
		
		order.addOrderDetail(orderDetail);
		order.setUser(user);
		order.setOrderId(100000l);
		
		pageable = PageRequest.of(0, 10);
		
		when(userService.userExistsByUsername(anyString())).thenReturn(true);
//		emailservice.setupEmailAPIKey("my-secret-pw");
	}
	
	//Annotation with Keycloak no longer works in Spring Boot 3 like @WithMockUser(username="admin", roles= "{CUSTOMER}")
	//This is an alternate solution
	void preAuthorization(boolean authorized) {
	     // Mocking Authentication
        Authentication auth = mock(Authentication.class);  
        when(auth.isAuthenticated()).thenReturn(authorized);
        SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	void isAuthenticatedByPrincipal(boolean authenticated) {
		when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), anyString())).thenReturn(authenticated);
	
	}
	
	@Test
	@WithMockUser
	void getOrderByIdTest() throws Exception {
		isAuthenticatedByPrincipal(true);	
		when(orderService.getOrderById(order.getOrderId())).thenReturn(order);
		mockMvc.perform(get("/order/id/"+order.getOrderId())
				.with(csrf()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.orderId", is(order.getOrderId()),Long.class));
	}
	
	@Test
	@WithMockUser
	void getOrderByIdTestFail() throws Exception {
		when(orderService.getOrderById(order.getOrderId())).thenReturn(order);
		mockMvc.perform(get("/order/id/"+order.getOrderId())
				.with(csrf()))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	@WithMockUser
	void getOrderByIdTestFail2() throws Exception {
		isAuthenticatedByPrincipal(true);	
		Long nonExistent = 123456l;
		when(orderService.getOrderById(nonExistent)).thenThrow(new OrderNotFoundException("Order doesn't exist"));
		mockMvc.perform(get("/order/id/"+nonExistent)
				.with(csrf()))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUser
	void getOrdersByUsername() throws Exception{
		isAuthenticatedByPrincipal(true);
		List <Order> orders = new ArrayList<Order>();
		orders.add(order);
		Page<Order> orderPage = new PageImpl<>(orders, pageable, orders.size());
		when(orderService.getAllOrdersByUsername(anyString(), any(Pageable.class))).thenReturn(orderPage);
		/*MvcResult result =*/ mockMvc.perform(get("/order/username/" + user.getUsername())
	            .with(csrf()))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.content", hasSize(1))).andReturn();
	}
	
	@Test
	@WithMockUser
	void getOrdersByUsernameFail() throws Exception{
		List <Order> orders = new ArrayList<Order>();
		orders.add(order);
		Page<Order> orderPage = new PageImpl<>(orders, pageable, orders.size());
		when(userService.userExistsByUsername(anyString())).thenReturn(false);
		when(orderService.getAllOrdersByUsername(anyString(),any(Pageable.class))).thenReturn(orderPage);
		mockMvc.perform(get("/order/username/"+user.getUsername())
				.with(csrf()))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUser
	void addOrder() throws Exception {
		isAuthenticatedByPrincipal(true);
		when(orderService.addOrder(any(Order.class))).thenReturn(order);
		mockMvc.perform(post("/order/username/"+user.getUsername())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.orderId", is(order.getOrderId()), Long.class));
	}
	
	@Test
	@WithMockUser
	void addOrderFail() throws Exception {
		String nonExistent = "non-existent";
		when(orderService.addOrder(any(Order.class))).thenThrow(new DataIntegrityViolationException("User doesn't exist"));
		mockMvc.perform(post("/order/username/"+nonExistent)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)))
		.andExpect(status().isNotFound());
	}
	
	@Test
	@WithMockUser
	void addOrderFail2() throws Exception {
		when(userService.userExistsByUsername(anyString())).thenReturn(false);
		String nonExistent = "non-existent";
		mockMvc.perform(post("/order/username/"+nonExistent)
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(order)))
		.andExpect(status().isNotFound());
	}

}
