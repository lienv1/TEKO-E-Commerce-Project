package apiserver.apiserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import apiserver.apiserver.exception.UserNotFoundException;
import apiserver.apiserver.model.Address;
import apiserver.apiserver.model.User;
import apiserver.apiserver.repo.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;

	public UserService(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	public List<User> getAllUser() {
		return userRepo.findAll();
	}

	public User addUser(User user) {
		try {
			return userRepo.save(user);
		} catch (DataIntegrityViolationException ex) {
			throw new RuntimeException("Username is already taken.");
		}
	}

	public User getUserByID(Long id) throws UserNotFoundException {
		return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	public User getUserByUsername(String username) throws UserNotFoundException {
		return userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	public User editUserByUsername(User user, String username) throws UserNotFoundException {
		User toUpdate = userRepo.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		User updatedUser = editUser(user, toUpdate);
		return updatedUser;
	}

	public User editUserById(User user, Long id) {
		User toUpdate = userRepo.getReferenceById(id);
		User updatedUser = editUser(user, toUpdate);
		return updatedUser;
	}

	public User editUser(User oldUser, User newUser) {
		try {
			Address billingAddress = oldUser.getBillingAddress();
			billingAddress.setId(newUser.getBillingAddress().getId());
			Address deliveryAddress = oldUser.getDeliveryAddress();
			deliveryAddress.setId(newUser.getDeliveryAddress().getId());
			newUser.setBillingAddress(billingAddress);
			newUser.setDeliveryAddress(deliveryAddress);
		} catch (NullPointerException e) {
			System.out.println("The user has not provided an address yet");
		}

		newUser.setEmail(oldUser.getEmail());
		newUser.setLastname(oldUser.getLastname());
		newUser.setFirstname(oldUser.getFirstname());
		return userRepo.save(newUser);
	}

}
