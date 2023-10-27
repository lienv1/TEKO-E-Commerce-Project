package apiserver.apiserver.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.security.Principal;

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

import apiserver.apiserver.model.Favorite;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.model.User;
import apiserver.apiserver.repo.FavoriteRepo;
import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.FavoriteService;

@WebMvcTest(FavoriteController.class)
class FavoriteControllerTest {
	
	@MockBean
	private FavoriteService favoriteService;

	@MockBean
	private AuthorizationService authorizationService;
	
	@Autowired
	private MockMvc mockMvc;
	
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
		product.setSubCategory("Sauce");
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
	
	//Annotation with Keycloak no longer works in Spring Boot 3 like @WithMockUser(username="admin", roles= "{CUSTOMER}")
	//This is an alternate solution
	void preAuthorization(boolean authorized) {
	     // Mocking Authentication
        Authentication auth = mock(Authentication.class);  
        when(auth.isAuthenticated()).thenReturn(authorized);
        SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	void isAuthenticatedByPrincipal(boolean authenticated) {
		when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), anyString())).thenReturn(authenticated);
	}

	@Test
	@WithMockUser
	void getFavoriteTestTrue() throws Exception {
		isAuthenticatedByPrincipal(true);
		when(favoriteService.isFavorite(user.getUsername(), product.getProductId())).thenReturn(true);
		mockMvc.perform(get("/favorite/product/"+product.getProductId()+"/username/"+user.getUsername())
				.with(csrf()).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().string("true"));
	}
	
	@Test
	@WithMockUser
	void getFavoriteTestFalse() throws Exception {
		isAuthenticatedByPrincipal(true);
		when(favoriteService.isFavorite(user.getUsername(), product.getProductId())).thenReturn(false);
		mockMvc.perform(get("/favorite/product/"+product.getProductId()+"/username/"+user.getUsername())
				.with(csrf()).contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().string("false"));
	}
	
	@Test
	@WithMockUser
	void addFavoriteTest() throws Exception {
		isAuthenticatedByPrincipal(true);
		when(favoriteService.addFavoriteByUsernameAndProductId(user.getUsername(),product.getProductId())).thenReturn(favorite);
		mockMvc.perform(post("/favorite/product/"+product.getProductId()+"/username/"+user.getUsername())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	void removeFavorite() throws Exception{
		isAuthenticatedByPrincipal(true);
		mockMvc.perform(delete("/favorite/product/"+product.getProductId()+"/username/"+user.getUsername())
				.with(csrf()))
		.andExpect(status().isOk());
	}
}
