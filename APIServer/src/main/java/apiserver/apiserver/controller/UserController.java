package apiserver.apiserver.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import apiserver.apiserver.model.User;
import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.UserService;
import exception.UserNotFoundException;

@Controller
@RequestMapping("/user")
public class UserController {

	private UserService userService;
	private final AuthorizationService authorizationService;

	public UserController(UserService userService) {
		this.userService = userService;
		authorizationService = new AuthorizationService();
	}

	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUser() {
		List<User> list = userService.getAllUser();
		return new ResponseEntity<List<User>>(list, HttpStatus.OK);
	}

	@PostMapping("/add")
	public ResponseEntity<User> addUser(@RequestBody User user) {
		try {
			User addedUser = userService.addUser(user);
			return ResponseEntity.ok(addedUser);
		} catch (Exception e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@PreAuthorize(value = "hasAnyRole('ADMIN','CUSTOMER')")
	@PutMapping("/update/username/{username}")
	public ResponseEntity<User> editUser(@PathVariable("username") String username, Principal principal, @RequestBody User updatedUser) {
		User editedUser;
		try {
			editedUser = userService.editUserByUsername(updatedUser, username);
			return new ResponseEntity<User>(editedUser, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/userinfo/username/{username}")
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
			System.out.println("User doesn't exist");
			return ResponseEntity.notFound().build();
		}
	}

}
