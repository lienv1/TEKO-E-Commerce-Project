package apiserver.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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
	private List<User> userList;
	
	private String username = "johndoe";
	
	@BeforeEach
	void init() {
		user = new User();
		user.setFirstname("John");
		user.setLastname("Doe");
		user.setEmail("john.doe@example.com");
		user.setUsername(username);
		
		userList = new ArrayList<User>();
		userList.add(user);
		
		when(userRepo.save(any(User.class))).thenReturn(user);
		when(userRepo.findByUsername(username)).thenReturn(Optional.of(user));
		when(userRepo.findAll()).thenReturn(userList);
	}

	@Test
	void testAddUser() {
		User addedUser = userService.addUser(user);
		assertEquals(addedUser, user);
	}
	
	@Test
	void testGetUserByUsername() throws UserNotFoundException {
		User john = userService.getUserByUsername(username);
		assertEquals(john, user);
	}
	
	@Test
	void getAllUsers() {
		List <User> allUser = userService.getAllUser();
		assertEquals(1,allUser.size());
		assertEquals(username, allUser.get(0).getUsername());
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
