package apiserver.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import apiserver.apiserver.exception.UserNotFoundException;
import apiserver.apiserver.model.User;
import apiserver.apiserver.repo.UserRepo;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class UserServiceTest {

	@Mock
	private UserRepo userRepo;
	
	@InjectMocks
	private UserService userService;
	
	private User user;
	private String username = "johndoe";
	
	@BeforeEach
	void init() {
		user = new User();
		user.setFirstname("John");
		user.setLastname("Doe");
		user.setEmail("john.doe@example.com");
		user.setUsername(username);
		when(userRepo.save(any(User.class))).thenReturn(user);
		when(userRepo.findByUsername(any(String.class))).thenReturn(Optional.of(user));
	}

	@Test
	void testAddUser() {
		User addedUser = userService.addUser(user);
		assertEquals(addedUser, user);
	}
	
	@Test
	void testGetUserByUsername() throws UserNotFoundException {
		when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
		User john = userService.getUserByUsername(username);
		assertEquals(john, user);
	}
	
	@Test
	void testUserNotFoundException() {
	    when(userRepo.findByUsername(anyString())).thenReturn(Optional.empty());
	    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
	        userService.getUserByUsername(username);
	    });
	    assertEquals("User not found", exception.getMessage());
	}
	
	@Test
	void testEditUserByUsername() throws UserNotFoundException {
		User originalUser = userService.addUser(this.user);
		String newFirstname = "John2";
		originalUser.setFirstname(newFirstname);
		User editedUser = userService.editUserByUsername(originalUser, username);
		assertEquals(newFirstname, editedUser.getFirstname());
	}
}
