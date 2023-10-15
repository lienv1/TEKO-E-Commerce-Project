package apiserver.apiserver.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import apiserver.apiserver.exception.ProductNotFoundException;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.repo.ProductRepo;
import apiserver.apiserver.service.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@MockBean
	private ProductService productService;

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

		when(productService.getAllProducts()).thenReturn(products);
	}

	@Test
	void testGetAllProducts() {
		List<Product> list = productService.getAllProducts();
		assertTrue(!list.isEmpty());
		Product product = list.get(0);
		assertNotNull(product);
		assertNotNull(product.getProductId());
	}

	@Test
	void testGetProductById() throws ProductNotFoundException {
		when(productService.getProductById(this.product.getProductId())).thenReturn(this.product);
		Product product = productService.getProductById(14326l);
		assertNotNull(product);
	}

	@Test
	void testGetProductByIdFail() throws ProductNotFoundException {
		when(productService.getProductById(this.product.getProductId()))
				.thenThrow(new ProductNotFoundException("Product doesn't exist"));

		ProductNotFoundException ex = assertThrows(ProductNotFoundException.class,
				() -> productService.getProductById(14326l));
		assertEquals("Product doesn't exist", ex.getMessage());
	}

	@Test
	void testAddProduct() {
		when(productService.addProduct(any(Product.class))).thenReturn(this.product);
		Product addedProduct = productService.addProduct(this.product);
		assertNotNull(addedProduct);
		assertNotNull(addedProduct.getProductId());
	}

	@Test
	void testEditProduct() {
		double newPrice = 6.60;
		product.setPrice(newPrice);
		when(productService.editProduct(any(Product.class))).thenReturn(product);
		Product editedProduct = productService.editProduct(product);
		assertNotNull(editedProduct);
		assertEquals(newPrice, editedProduct.getPrice());
	}

	@Test
	void testDeleteProduct() throws ProductNotFoundException {
		this.product.setDeleted(true);
		when(productService.deleteProduct(product.getProductId())).thenReturn(this.product);
		Product deletedProduct = productService.deleteProduct(this.product.getProductId());
		assertTrue(deletedProduct.isDeleted());
	}

	@Test
	void testDeleteProductFail() throws ProductNotFoundException {
		Long nonExistendId = 12345l;
		when(productService.deleteProduct(nonExistendId))
				.thenThrow(new ProductNotFoundException("Can not find product with the id " + nonExistendId));
		ProductNotFoundException ex = assertThrows(ProductNotFoundException.class,
				() -> productService.deleteProduct(nonExistendId));
		assertEquals("Can not find product with the id " + nonExistendId, ex.getMessage());
	}

}
