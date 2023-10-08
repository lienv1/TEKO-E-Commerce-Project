package apiserver.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import apiserver.apiserver.exception.UserNotFoundException;
import apiserver.apiserver.model.User;
import apiserver.apiserver.repo.UserRepo;

@SpringBootTest
@RunWith(SpringRunner.class)
class UserServiceTest {
	
	@Autowired
	private UserService userService;

	@MockBean
	private UserRepo userRepo;
	
	private User user = new User();
	
	@BeforeEach
	void init() {
		user.setFirstname("John");
		user.setLastname("Doe");
		user.setEmail("john.doe@example.com");
		user.setUsername("johndoe2");
	}

	@Test
	 void testAddUser() {
		when(userRepo.save(any(User.class))).thenReturn(user);
		User addedUser = userService.addUser(user);
		assertEquals(addedUser, user);
	}
	
	@Test
	void testGetUserByUsername() throws UserNotFoundException {
		when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
		User john = userService.getUserByUsername("johndoe2");
		assertEquals(john, user);
	}
	
	@Test
	void testUserNotFoundException() {
	    when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
	    // Use assertThrows to test for the exception
	    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
	        userService.getUserByUsername("johndoe2");
	    });
	    assertEquals("User not found", exception.getMessage());
	}
	
	@Test
	void testEditUserByUsername() throws UserNotFoundException {
		String newName = "John2";
		user.setFirstname(newName);
		when(userRepo.save(any(User.class))).thenReturn(user);
		when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
		user = userService.editUserByUsername(user, user.getUsername());
		assertEquals(user.getFirstname(), newName);
	}
}
