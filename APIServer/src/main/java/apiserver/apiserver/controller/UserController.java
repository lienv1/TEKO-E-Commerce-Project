package apiserver.apiserver.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import apiserver.apiserver.exception.UserNotFoundException;
import apiserver.apiserver.model.User;
import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorizationService authorizationService;

	public UserController(UserService userService,AuthorizationService authorizationService) {
		this.userService = userService;
		this.authorizationService = authorizationService;
	}

	@GetMapping("/all")
	@PreAuthorize(value= "hasRole('ADMIN')")
	public ResponseEntity<List<User>> getAllUser() {
		List<User> list = userService.getAllUser();
		return new ResponseEntity<List<User>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/username/{username}")
	@PreAuthorize(value = "hasAnyRole('ADMIN','CUSTOMER')")
	public ResponseEntity<User> getUser(@PathVariable("username") String username, Principal principal) {
		try {
			boolean isAuthorizied = this.authorizationService.isAuthenticatedByPrincipal(principal, username);
			if (!isAuthorizied) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
			User user = userService.getUserByUsername(username);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity("User doesn't exist",HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/add")
	public ResponseEntity<User> addUser(@RequestBody User user) {
		try {
			User addedUser = userService.addUser(user);
			return new ResponseEntity<User>(addedUser,HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize(value = "hasAnyRole('ADMIN','CUSTOMER')")
	@PutMapping("/update/username/{username}")
	public ResponseEntity<User> editUser(@PathVariable("username") String username, Principal principal, @RequestBody User updatedUser) {
		try {	
			boolean isAuthorizized = authorizationService.isAuthenticatedByPrincipal(principal, username);
			
			if (!isAuthorizized)
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			User editedUser = userService.editUserByUsername(updatedUser, username);
			return new ResponseEntity<User>(editedUser, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return ResponseEntity.notFound().build();
	}

	@PreAuthorize(value = "hasAnyRole('ADMIN','CUSTOMER')")
	@DeleteMapping("/update/username/{username}")
	public ResponseEntity<User> deleteUser(@PathVariable("username") String username, Principal principal){
		try {
			boolean isAuthorizized = authorizationService.isAuthenticatedByPrincipal(principal, username);
			
			if (!isAuthorizized)
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			User userToDelete = userService.deleteUserByUsername(username);
			return new ResponseEntity<User>(userToDelete, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			System.out.println(e.getMessage());
			return ResponseEntity.notFound().build();
		}
	}
}
