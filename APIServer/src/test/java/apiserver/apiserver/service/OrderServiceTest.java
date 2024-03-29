package apiserver.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import apiserver.apiserver.exception.OrderNotFoundException;
import apiserver.apiserver.exception.UserNotFoundException;
import apiserver.apiserver.model.Order;
import apiserver.apiserver.model.OrderDetail;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.model.User;
import apiserver.apiserver.repo.OrderRepo;
import apiserver.apiserver.repo.ProductRepo;
import apiserver.apiserver.repo.UserRepo;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class OrderServiceTest {

	@Mock
	private OrderRepo orderRepo;
	
	@Mock
	private UserRepo userRepo;
	
	@Mock
	private ProductRepo productRepo;
	
	@InjectMocks
	private OrderService orderService;
	
	@InjectMocks
	private UserService userService;
	
	@InjectMocks
	private ProductService productService;
	
	private User user;
	
	private Product product;
	
	private Order order;
	
	private OrderDetail orderDetail;
	
	@BeforeEach
	void init() {
		user = new User();
		user.setUsername("johndoe");
		user.setUserId(1000l);
		userRepo.saveAndFlush(user);
		
		product = new Product();
		product.setProductId(14326l);
		product.setProductName("Sriracha Hot Chili Sauce");
		productRepo.saveAndFlush(product);
		
		orderDetail = new OrderDetail();
		orderDetail.setProduct(product);
		orderDetail.setQuantity(2);
		
		order = new Order();
		
		order.addOrderDetail(orderDetail);
		order.setUser(user);
		order.setOrderId(100000l);
	}
	

	@Test
	void addOrder() throws Exception {
		when(orderRepo.save(order)).thenReturn(order);
		when(userRepo.saveAndFlush(any(User.class))).thenReturn(user);
		Order addedOrder = orderService.addOrder(order);
		assertNotNull(addedOrder.getOrderId());
		assertEquals(order.getOrderId(), addedOrder.getOrderId());
	}
	

	@Deprecated
	void addOrderFail() throws Exception{
//	    User nonExistentUser = new User();
//	    nonExistentUser.setUsername("non-existent");
//	    order.setUser(nonExistentUser);
//	    when(orderService.addOrder(order)).thenThrow(new DataIntegrityViolationException("User doesn't exist"));
//	    DataIntegrityViolationException e = assertThrows(DataIntegrityViolationException.class , () -> {
//	        orderService.addOrder(order);
//	    });
//	    assertEquals("User doesn't exist", e.getMessage());
	}
	
	@Test
	void getOrderById() throws OrderNotFoundException {
		when(orderRepo.findById(order.getOrderId())).thenReturn(Optional.of(order));
		Order retrievedOrder = orderService.getOrderById(order.getOrderId());		
		assertNotNull(retrievedOrder);
		assertEquals(order.getOrderId(), retrievedOrder.getOrderId());
	}
	

	@Deprecated
	void getAllOrdersByUsername() {
//		List<Order> addedOrders = new ArrayList<Order>();
//		addedOrders.add(order);
//		when(orderRepo.findByUserUsername(user.getUsername())).thenReturn(addedOrders);
//		List<Order> orderList = orderService.getAllOrdersByUsername(user.getUsername());
//		assertEquals(1, orderList.size());
//		assertEquals(product.getProductId(), orderList.get(0).getOrderDetails().iterator().next().getProduct().getProductId());
	}
	

	@Deprecated
	void getAllOrdersByUsernameFail(){
//		String nonExistent = "non-existent";
//		when(orderRepo.findByUserUsername(nonExistent)).thenReturn(new ArrayList<Order>());
//		List<Order> orderList = orderService.getAllOrdersByUsername(nonExistent);
//		assertTrue(orderList.isEmpty());
	}
	
	

}
