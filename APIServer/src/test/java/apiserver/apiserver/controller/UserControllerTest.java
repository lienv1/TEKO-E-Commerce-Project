package apiserver.apiserver.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import apiserver.apiserver.model.User;
import apiserver.apiserver.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class) 
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Mock the service used by the controller
    private UserService userService;
    
    User user1 = new User();
    User user2 = new User();
    
    @BeforeEach
    void init() {
         user1.setFirstname("John");
         user1.setLastname("Doe");
         user2.setFirstname("Jane");
         user2.setLastname("Doe");
    }

    @Test
    @WithMockUser(username="admin", roles= "{ADMIN}")
    public void testGetAllUsers() throws Exception {
        // Prepare data and mock's behavior
        List<User> userList = Arrays.asList(user1, user2);
        when(userService.getAllUser()).thenReturn(userList);
        // Perform GET Request
        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstname", is("John")))
                .andExpect(jsonPath("$[1].firstname", is("Jane")));
    }
    
    @Test
    @WithMockUser(roles= "{CUSTOMER}")
    public void testGetAllUsersUnauthorized() throws Exception {
        // Prepare data and mock's behavior
        List<User> userList = Arrays.asList(user1, user2);
        when(userService.getAllUser()).thenReturn(userList);
        // Perform GET Request
        mockMvc.perform(get("/user/all"))
                .andExpect(status().isUnauthorized());
    }
    
    @Test	
    @WithMockUser
    public void testAddUser() throws Exception {
        // Prepare data and mock's behavior
        User savedUser = new User();
        savedUser.setFirstname("John");
        savedUser.setLastname("Doe");
        savedUser.setId(1L); // assuming the user is assigned an ID of 1 upon saving
        
        when(userService.addUser(any(User.class))).thenReturn(savedUser);
        
        // Perform POST Request
        mockMvc.perform(post("/user/add")
        		.with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstname", is("John")))
                .andExpect(jsonPath("$.lastname", is("Doe")));
    }
}
