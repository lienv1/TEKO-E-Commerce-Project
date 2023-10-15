package apiserver.apiserver.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import apiserver.apiserver.model.Product;
import apiserver.apiserver.repo.ProductRepo;
import apiserver.apiserver.service.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {
	
	@MockBean
	private ProductService productService;
	
	@MockBean
	private ProductRepo productRepo;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private Product product;
	private List<Product> products;
	
	@BeforeEach
	void initProduct() {
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
		
		products = new ArrayList<Product>();
		products.add(product);
		
		when(productService.addProduct(any(Product.class))).thenReturn(product);
		//when(productService.getAllProducts()).thenReturn(products);
	}

	@Test
	void testGetAllProducts() {
		//productService.addProduct(product);
		productRepo.saveAndFlush(product);
		List <Product> list = productRepo.findAll();
		assertEquals(1,list.size());
	}

	@Test
	void testGetProductById() {
		fail("Not yet implemented");
	}

	@Test
	void testAddProduct() {
		fail("Not yet implemented");
	}

	@Test
	void testEditProduct() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteProduct() {
		fail("Not yet implemented");
	}

}
