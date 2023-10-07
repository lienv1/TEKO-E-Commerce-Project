package apiserver.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import apiserver.apiserver.model.User;
import apiserver.apiserver.repo.UserRepo;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceTest {
	
	@Autowired
	private UserService userService;

	@MockBean
	private UserRepo userRepo;

	@Test
	public void testAddUser() {
		User user = new User();
		user.setFirstname("John");
		user.setLastname("Doe");
		user.setEmail("john.doe@example.com");
		user.setUsername("johndoe2");
		when(userRepo.save(any(User.class))).thenReturn(user);
		User addedUser = userService.addUser(user);
		assertEquals(addedUser, user);
	}

}
