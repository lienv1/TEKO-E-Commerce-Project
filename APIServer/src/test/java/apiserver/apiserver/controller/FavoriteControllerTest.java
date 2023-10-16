package apiserver.apiserver.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import apiserver.apiserver.model.Favorite;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.model.User;
import apiserver.apiserver.service.FavoriteService;

@WebMvcTest(FavoriteController.class)
class FavoriteControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private FavoriteService favoriteService;

	private Favorite favorite;
	
	private User user;
	
	private Product product;
	
	@BeforeEach
	void init() {
		initProduct();
		initUser();
		
		favorite = new Favorite();
		favorite.setProduct(product);
		favorite.setUser(user);
	}
	
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
	}
	
	void initUser() {
		user = new User();
		user.setUsername("johndoe");
		user.setFirstname("John");
		user.setLastname("Doe");
	}
	
	@Test
	void getFavoriteTest() {
		when(get("/favorite/product/id/"+product.getProductId()));
	}
}
