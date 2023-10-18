package apiserver.apiserver.repo;

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
	
	private User user;
	
	private Product product;
	
	private Order order;
	
	private OrderDetail orderDetail;
	

	@BeforeEach
	void init() {
		user = new User();
		user.setUsername("johndoe");
		user.setUserId(1000l);
		
		product = new Product();
		product.setProductId(14326l);
		product.setProductName("Sriracha Hot Chili Sauce");
		
		orderDetail = new OrderDetail();
		orderDetail.setId(100000l);
		orderDetail.setProduct(product);
		orderDetail.setQuantity(2);
		
		order = new Order();
		order.setOrderId(100000l);
		order.addOrderDetail(orderDetail);
		
	}
	
	@Test
	void saveOrderTest() {
		
	}
	
	
}
