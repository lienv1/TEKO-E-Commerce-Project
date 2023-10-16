package apiserver.apiserver.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import apiserver.apiserver.exception.UserNotFoundException;
import apiserver.apiserver.model.User;
import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@MockBean // Mock the service used by the controller
	private UserService userService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private AuthorizationService authorizationService;

	private User user1;
	private User user2;

	@BeforeEach
	void init() throws UserNotFoundException {
		user1 = new User();
		user1.setUsername("johndoe");
		user1.setFirstname("John");
		user1.setLastname("Doe");
		
		user2 = new User();
		user2.setUsername("janedoe");
		user2.setFirstname("Jane");
		user2.setLastname("Doe");
		
		when(userService.addUser(any(User.class))).thenAnswer(element -> {
			User savedUser = user1;
			savedUser.setUserId(1l);
			return savedUser;
		});	
	}
	
	void initUser2() throws UserNotFoundException {
		when(userService.addUser(user2)).thenAnswer(element -> {
			User savedUser = user2;
			savedUser.setUserId(2l);
			return savedUser;
		});
		when(userService.getUserByUsername(user2.getUsername())).thenReturn(user2);
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
	void getAllUsersTest() throws Exception {
		preAuthorization(true);
		// Prepare data and mock's behavior
		List<User> userList = Arrays.asList(user1, user2);
		when(userService.getAllUser()).thenReturn(userList);
		// Perform GET Request
		mockMvc.perform(get("/user/all")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].firstname", is("John"))).andExpect(jsonPath("$[1].firstname", is("Jane")));
	}

	@Test
    void getAllUsersTestForbidden() throws Exception {
		preAuthorization(false);
        // Prepare data and mock's behavior
        List<User> userList = Arrays.asList(user1, user2);
        when(userService.getAllUser()).thenReturn(userList);
        // Perform GET Request
        mockMvc.perform(get("/user/all"))
                .andExpect(status().isForbidden());
    }

    @Test
	void getAllUsersTestUnauthorized() throws Exception {
		mockMvc.perform(get("/user/all")).andExpect(status().isUnauthorized());
	}
    
    @Test
    @WithMockUser
    void getUserTest() throws Exception{
    	when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), any(String.class))).thenReturn(true);
    	mockMvc.perform(get("/user/username/"+user1.getUsername())
    			.with(csrf())
    			.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isOk())
    	.andDo(print());
    }
    
    @Test
    @WithMockUser
    void getUserTestFail() throws Exception {
    	String nonExistent = "non-existent-user";
    	when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), any(String.class))).thenReturn(true);
    	when(userService.getUserByUsername(nonExistent)).thenThrow(new UserNotFoundException ("User not found"));
    	String result = mockMvc.perform(get("/user/username/"+nonExistent)
    			.with(csrf())
    			.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isNotFound())
    	.andDo(print()).andReturn().getResponse().getContentAsString();
    	assertEquals("User doesn't exist",result);
    }
    
    @Test
    @WithMockUser
    void getUserTestFail2() throws Exception {
    	when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), any(String.class))).thenReturn(false);
    	when(userService.getUserByUsername(user1.getUsername())).thenThrow(new UserNotFoundException ("User not found"));
    	mockMvc.perform(get("/user/username/"+user1.getUsername())
    			.with(csrf())
    			.contentType(MediaType.APPLICATION_JSON))
    	.andExpect(status().isUnauthorized())
    	.andDo(print());
    }


	@Test
	void addUserTest() throws Exception {	
		preAuthorization(true);
	    mockMvc.perform(post("/user/add")
	            .with(csrf())
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(user1)))
	    		.andDo(print())
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.userId", is(1)))
	            .andExpect(jsonPath("$.firstname", is("John")))
	            .andExpect(jsonPath("$.lastname", is("Doe")));
	}
	
	@Test
	@WithMockUser
	void editUserTest() throws Exception {
		String newFirstname = "John2";
		user1.setFirstname(newFirstname);
		
		when(userService.editUserByUsername(any(User.class), any(String.class))).thenReturn(user1);
		//has either ADMIN role or is authorized user;
		when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), any(String.class))).thenReturn(true);
		mockMvc.perform(put("/user/update/username/"+user1.getUsername())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user1)))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.firstname", is(newFirstname)));
	}
	
	@Test
	@WithMockUser
	void editUserTestUnauthorized() throws Exception {
		String newFirstname = "John2";
		user1.setFirstname(newFirstname);
		when(userService.editUserByUsername(any(User.class), any(String.class))).thenReturn(user1);
		mockMvc.perform(put("/user/update/username/"+user1.getUsername())
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(user1)))
		.andDo(print())
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	@WithMockUser
	void deleteUserTest() throws Exception {
		when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), any(String.class))).thenReturn(true);
		mockMvc.perform(delete("/user/delete/username/"+this.user1.getUsername())
				.with(csrf()))
		.andDo(print())
		.andExpect(status().isOk());
	}
	
	@Test
	@WithMockUser
	void deleteUserTestFail() throws Exception {
		when(authorizationService.isAuthenticatedByPrincipal(any(Principal.class), any(String.class))).thenReturn(false);
		mockMvc.perform(delete("/user/delete/username/"+this.user1.getUsername())
				.with(csrf()))
		.andDo(print())
		.andExpect(status().isUnauthorized());
	}
	
}