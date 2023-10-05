package apiserver.apiserver;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import apiserver.apiserver.model.User;
import apiserver.apiserver.service.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserControllerTests {


	@MockBean
	private UserService userService;

	@Test
	public void testInitUser() throws Exception {
		User user = new User();
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        
        assertEquals(user.getUsername(), "johndoe");
        assertEquals(user.getFirstname(), "John");
        assertEquals(user.getLastname(), "Doe");
        assertEquals(user.getEmail(), "john.doe@example.com");
	}

}
