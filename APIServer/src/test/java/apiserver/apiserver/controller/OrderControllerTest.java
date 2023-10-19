package apiserver.apiserver.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import apiserver.apiserver.exception.OrderNotFoundException;
import apiserver.apiserver.model.Order;
import apiserver.apiserver.model.OrderDetail;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.model.User;
import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.OrderService;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

	@MockBean
	private OrderService orderService;
	
	@MockBean
	private AuthorizationService authorizationService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private User user;
	private Product product;
	private Order order;
	
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
	void getOrderByIdTest() throws Exception {
		isAuthenticatedByPrincipal(true);
		
		when(orderService.getOrderById(order.getOrderId())).thenReturn(order);
		mockMvc.perform(get("/order/id/"+order.getOrderId()).with(csrf()))
		.andExpect(status().isOk());
	}

}
