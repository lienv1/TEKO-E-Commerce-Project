package apiserver.apiserver.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import apiserver.apiserver.exception.UserNotFoundException;
import apiserver.apiserver.model.User;
import apiserver.apiserver.repo.UserRepo;
import apiserver.apiserver.service.UserService;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@MockBean // Mock the service used by the controller
	private UserService userService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

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
		when(userService.getUserByUsername(user1.getUsername())).thenReturn(user1);
	}
	
	void initUser2() throws UserNotFoundException {
		when(userService.addUser(user2)).thenAnswer(element -> {
			User savedUser = user2;
			savedUser.setUserId(2l);
			return savedUser;
		});
		when(userService.getUserByUsername(user2.getUsername())).thenReturn(user2);
	}

	@Test
	@WithMockUser(username = "admin", roles = "{ADMIN}")
	void testGetAllUsers() throws Exception {
		// Prepare data and mock's behavior
		List<User> userList = Arrays.asList(user1, user2);
		when(userService.getAllUser()).thenReturn(userList);
		// Perform GET Request
		mockMvc.perform(get("/user/all")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].firstname", is("John"))).andExpect(jsonPath("$[1].firstname", is("Jane")));
	}

 	//Partial Keycloak adapters no longer work in Spring Boot 3
	//@Test
    @Deprecated
    @WithMockUser(username="admin", roles= "{CUSTOMER}")
    public void testGetAllUsers2() throws Exception {
        // Prepare data and mock's behavior
        List<User> userList = Arrays.asList(user1, user2);
        when(userService.getAllUser()).thenReturn(userList);
        // Perform GET Request
        mockMvc.perform(get("/user/all"))
                .andExpect(status().isForbidden());
    }

    @Test
	void testGetAllUsersUnauthorized() throws Exception {
		// Perform GET Request
		mockMvc.perform(get("/user/all")).andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser(username = "admin", roles = "{ADMIN}")
	void testAddUser() throws Exception {	
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
}