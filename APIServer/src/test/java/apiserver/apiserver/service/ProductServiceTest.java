package apiserver.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.repo.ProductRepo;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ProductServiceTest {
	
	@Mock
	private ProductRepo productRepo;
	
	@InjectMocks
	private ProductService productService;
	
	private Product product;
	
	private List<Product> productList;
	
	@BeforeEach
	void setUpProduct() {
		product = new Product();
		product.setProductId(14326l);
		product.setProductName("Sriracha Hot Chili Sauce");
		product.setBrand("Huy Fong Foods");
		product.setWeight(481);
		product.setCategory("Dry Goods");
		product.setProductGroup("Sauce");
		product.setPack(12);
		product.setGtinUnit("024463061095");
		product.setGtinPack("024463101098");
		product.setPrice(6.50);
		product.setStock(120);
		product.setOrigin("Thailand");
		productList = new ArrayList<Product>();
		productList.add(product);
	}

	@Test
	void getAllProductTest() {
		when(productRepo.save(any(Product.class))).thenReturn(product);
		productService.addProduct(product);
		when(productRepo.findAll()).thenReturn(productList);
		List<Product> list = productService.getAllProducts();
		assertEquals(1,list.size());
		assertEquals(14326l,list.get(0).getProductId());
		assertEquals("Sriracha Hot Chili Sauce", list.get(0).getProductName());
	}
	
	@Test
	void getProductByIdTest() {
		when(productRepo.save(any(Product.class))).thenReturn(product);
		productService.addProduct(product);
		when(productRepo.findById(product.getProductId())).thenReturn(Optional.of(product));
		Optional<Product> toRetrieve = productRepo.findById((product.getProductId()));
		assertTrue(toRetrieve.isPresent());
		assertEquals(product.getProductId(), toRetrieve.get().getProductId());
		assertEquals(product.getProductName(), toRetrieve.get().getProductName());
	}
	
	@Test
	void testProductEdit() {
		when(productRepo.save(any(Product.class))).thenReturn(this.product);
		Product product = productService.addProduct(this.product);
		product.setPrice(6.60);
		//TODO
	}

}
