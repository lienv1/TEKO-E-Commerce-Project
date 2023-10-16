package apiserver.apiserver.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import apiserver.apiserver.exception.ProductNotFoundException;
import apiserver.apiserver.model.Product;
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
	@WithMockUser
	void testGetAllProducts() throws Exception {
		mockMvc.perform(get("/product/all")
				.with(csrf()))
		.andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	void testGetProductById() throws Exception {
		when(productService.getProductById(product.getProductId())).thenReturn(product);
		mockMvc.perform(get("/product/id/"+product.getProductId()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.productId",is(product.getProductId()),Long.class))
		.andDo(print());
	}

//	@Test
	void testGetProductByIdFail() throws ProductNotFoundException {
		
	}

//	@Test
	void testAddProduct() {

	}

//	@Test
	void testEditProduct() {
	}

//	@Test
	void testDeleteProduct() throws ProductNotFoundException {

	}

//	@Test
	void testDeleteProductFail() throws ProductNotFoundException {

	}

}
