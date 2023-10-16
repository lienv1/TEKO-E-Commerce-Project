package apiserver.apiserver.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import apiserver.apiserver.exception.ProductNotFoundException;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.ProductService;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

	@MockBean
	private ProductService productService;
	
	@MockBean
	private AuthorizationService authorizationService;

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

	//Annotation with Keycloak no longer works in Spring Boot 3 like @WithMockUser(username="admin", roles= "{CUSTOMER}")
	//This is an alternate solution
	void preAuthorization(boolean authorized) {
	     // Mocking Authentication
        Authentication auth = mock(Authentication.class);  
        when(auth.isAuthenticated()).thenReturn(authorized);
        SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@Test
	void testGetAllProducts() throws Exception {
		preAuthorization(true);
		mockMvc.perform(get("/product/all")
				.with(csrf()))
		.andExpect(status().isOk());
	}

	@Test
	void testGetProductById() throws Exception {
		preAuthorization(true);
		when(productService.getProductById(product.getProductId())).thenReturn(product);
		mockMvc.perform(get("/product/id/"+product.getProductId()))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.productId",is(product.getProductId()),Long.class))
		.andExpect(jsonPath("$.productName",is(product.getProductName())));
	}

	@Test
	void testGetProductByIdFail() throws Exception {
		preAuthorization(true);
		Long nonExistentProduct = 34567l;
		when(productService.getProductById(nonExistentProduct)).thenThrow(new ProductNotFoundException("Product doesn't exist"));
		mockMvc.perform(get("/product/id/"+nonExistentProduct))
		.andExpect(status().isNotFound());
	}

	@Test
	@WithMockUser
	void testAddProduct() throws Exception {
		when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), any(String.class))).thenReturn(true);
		Product newProduct = new Product();
		newProduct.setProductId(26527l);
		newProduct.setProductName("Tiger Bier");
		when(productService.addProduct(any(Product.class))).thenReturn(newProduct);
		mockMvc.perform(post("/product/add")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProduct))
				)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.productId", is(newProduct.getProductId()),Long.class))
		.andExpect(jsonPath("$.productName", is(newProduct.getProductName())));
	}
	
	@Test
	@WithMockUser
	void testAddProductFail() throws Exception {
		when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), any(String.class))).thenReturn(false);
		Product newProduct = new Product();
		newProduct.setProductId(26527l);
		newProduct.setProductName(null);
		mockMvc.perform(post("/product/add")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProduct))
				)
		.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void testEditProduct() throws Exception {
		when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), any(String.class))).thenReturn(true);
		product.setPrice(6.60);
		when(productService.editProduct(any(Product.class))).thenReturn(product);
		mockMvc.perform(put("/product/id/"+product.getProductId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(product))
				)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.productId", is(product.getProductId()),Long.class))
		.andExpect(jsonPath("$.price", is(product.getPrice())));
	}
	
	@Test
	@WithMockUser
	void testEditProductFail() throws Exception {
		when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), any(String.class))).thenReturn(true);
		Product nonExistentProduct = new Product();
		nonExistentProduct.setProductId(98765l);
		when(productService.editProduct(any(Product.class))).thenThrow(new ProductNotFoundException("Product doesn't exist"));
		mockMvc.perform(put("/product/id/"+nonExistentProduct.getProductId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(nonExistentProduct))
				)
		.andExpect(status().isNotFound());
	}
	
	
	@Test
	@WithMockUser
	void testEditProductFail2() throws Exception {
		when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), any(String.class))).thenReturn(false);
		Product nonExistentProduct = new Product();
		nonExistentProduct.setProductId(98765l);
		when(productService.editProduct(any(Product.class))).thenThrow(new ProductNotFoundException("Product doesn't exist"));
		mockMvc.perform(put("/product/id/"+nonExistentProduct.getProductId())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(nonExistentProduct))
				)
		.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void testDeleteProduct() throws Exception {
		when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), any(String.class))).thenReturn(true);
		when(productService.deleteProduct(product.getProductId())).thenReturn(product);
		product.setDeleted(true);
		mockMvc.perform(delete("/product/id/"+product.getProductId())
				.with(csrf())
				)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.deleted", is(true)));
	}
	
	@Test
	@WithMockUser
	void testDeleteProductFail() throws Exception {
		when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), any(String.class))).thenReturn(false);
		when(productService.deleteProduct(product.getProductId())).thenReturn(product);
		product.setDeleted(true);
		mockMvc.perform(delete("/product/id/"+product.getProductId())
				.with(csrf())
				)
		.andExpect(status().isUnauthorized());
	}
	
}
