package apiserver.apiserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import apiserver.apiserver.model.User;
import apiserver.apiserver.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<User>> getAllUser(){	
		List<User> list = userService.getAllUser();
		return new ResponseEntity<List<User>>(list,HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<User> addUser(@RequestBody User user){
		  try {
	            User addedUser = userService.addUser(user);
	            return ResponseEntity.ok(addedUser);
	        } catch (Exception e) {
	            return new ResponseEntity(e.getMessage(),HttpStatus. BAD_REQUEST);
	        }
	}
	
	@PutMapping("/update/{userId}")
	public ResponseEntity<User> editUser(@PathVariable Long userId, @RequestBody User updatedUser){
			User editedUser = userService.editUser(updatedUser, userId);
			return new ResponseEntity<User>(editedUser,HttpStatus.OK);
	}
	

}
