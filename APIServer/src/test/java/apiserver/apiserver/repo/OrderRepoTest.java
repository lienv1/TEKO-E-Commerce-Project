package apiserver.apiserver.repo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import apiserver.apiserver.model.Order;
import apiserver.apiserver.model.OrderDetail;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
public class OrderRepoTest {
	
	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private UserRepo userRepo;
	
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
		
	}
	
	@Test
	@Deprecated
	void saveOrderTest() {
//		Order addedOrder = orderRepo.save(order);
//		assertNotNull(addedOrder.getOrderId());
//		assertNotNull(addedOrder.getUser().getUserId());
//		assertNotNull(addedOrder.getCreated());
//		List<OrderDetail> details = new ArrayList<OrderDetail>(addedOrder.getOrderDetails());
//		assertEquals(product.getProductId(), details.get(0).getProduct().getProductId());
//		assertEquals(user.getUserId(), addedOrder.getUser().getUserId());
//		assertEquals(orderDetail.getQuantity() ,details.get(0).getQuantity());
	}
	
	@Test
	@Deprecated
	void findOrderByIdTest() {
//		Long orderId = orderRepo.save(order).getOrderId();
//		Optional<Order> retrievedOrder = orderRepo.findById(orderId);
//		assertTrue(retrievedOrder.isPresent());
	}
	
	@Test 
	void findOrderByIdTestFail(){
		Long nonExistent = 12345678l;
		Optional<Order> retrievedOrder = orderRepo.findById(nonExistent);
		assertFalse(retrievedOrder.isPresent());
	}
	
	@Test
	@Deprecated
	void getOrdersByUsername() {
//		orderRepo.save(order);
//		List<Order> orders = orderRepo.findByUserUsername(user.getUsername());
//		assertEquals(1, orders.size());
//		assertEquals(user.getUsername(), orders.get(0).getUser().getUsername());
	}
	
	@Test
	@Deprecated
	void getOrdersByUsernameFail() {
		/*String nonExistent = "non-existent";
		List<Order> orders = orderRepo.findByUserUsername(nonExistent);
		assertTrue(orders.isEmpty());*/
	}
	
}
