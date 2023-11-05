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

	public User addUser(User user) throws DataIntegrityViolationException {
			return userRepo.save(user);
	}

	public User getUserByID(Long id) throws UserNotFoundException {
		return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	public User getUserByUsername(String username) throws UserNotFoundException {
		return userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	public User editUserByUsername(User newUser, String username) throws UserNotFoundException {
		User oldUser = userRepo.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException("User not found"));
		User updatedUser = editUser(newUser, oldUser);
		return updatedUser;
	}

	public User editUserById(User newUser, Long id) {
		User oldUser = userRepo.getReferenceById(id);
		User updatedUser = editUser(newUser, oldUser);
		return updatedUser;
	}

	public User editUser(User newUser, User oldUser) {
		try {
			Address billingAddress = newUser.getBillingAddress();
			billingAddress.setAddressId(oldUser.getBillingAddress().getAddressId());
			Address deliveryAddress = newUser.getDeliveryAddress();
			deliveryAddress.setAddressId(oldUser.getDeliveryAddress().getAddressId());
			oldUser.setBillingAddress(billingAddress);
			oldUser.setDeliveryAddress(deliveryAddress);
		} catch (NullPointerException e) {
			System.out.println("The user has not provided an address yet");
		}

		oldUser.setEmail(newUser.getEmail());
		oldUser.setLastname(newUser.getLastname());
		oldUser.setFirstname(newUser.getFirstname());
		oldUser.setPhone(newUser.getPhone());
		return userRepo.save(oldUser);
	}

	public User deleteUserByUsername(String username) throws UserNotFoundException {
		User user = userRepo.findByUsername(username).orElseThrow( () -> new UserNotFoundException("User not found") );
		user.setDeleted(true);
		return userRepo.save(user);
	}
	
}
