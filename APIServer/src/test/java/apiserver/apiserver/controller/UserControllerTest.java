package apiserver.apiserver.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import apiserver.apiserver.model.User;
import apiserver.apiserver.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class) 
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Mock the service used by the controller
    private UserService userService;

    @Test
    @WithMockUser(username="user", roles= "{ADMIN}")
    public void testGetAllUsers() throws Exception {
        // Prepare data and mock's behavior
        User user1 = new User();
        user1.setFirstname("John");
        user1.setLastname("Doe");
        User user2 = new User();
        user2.setFirstname("Jane");
        user2.setLastname("Doe");
        List<User> userList = Arrays.asList(user1, user2);

        when(userService.getAllUser()).thenReturn(userList);

        // Perform GET Request
        mockMvc.perform(get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstname", is("John")))
                .andExpect(jsonPath("$[1].firstname", is("Jane")));
    }
}
