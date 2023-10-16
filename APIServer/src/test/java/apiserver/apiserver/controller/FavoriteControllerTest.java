package apiserver.apiserver.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import apiserver.apiserver.service.FavoriteService;

@WebMvcTest(FavoriteController.class)
class FavoriteControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private FavoriteService favoriteService;

	@Test
	void test() {
		fail("Not yet implemented");
	}

}
