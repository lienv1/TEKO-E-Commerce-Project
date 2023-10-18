package apiserver.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import apiserver.apiserver.exception.OrderNotFoundException;
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
	void addOrder() {
		when(orderRepo.save(order)).thenReturn(order);
		Order addedOrder = orderService.addOrder(order);
		assertNotNull(addedOrder.getOrderId());
		assertEquals(order.getOrderId(), addedOrder.getOrderId());
	}
	
	@Test
	void getOrderById() throws OrderNotFoundException {
		when(orderRepo.findById(order.getOrderId())).thenReturn(Optional.of(order));
		Order retrievedOrder = orderService.getOrderById(order.getOrderId());		
		assertNotNull(retrievedOrder);
		assertEquals(order.getOrderId(), retrievedOrder.getOrderId());
	}

}
