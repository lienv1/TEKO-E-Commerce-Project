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
		User toUpdate = userRepo.findByUsername(username).orElseThrow( () -> new UserNotFoundException("User not found") );
		
		Address billingAddress = user.getBillingAddress();
		billingAddress.setId(toUpdate.getBillingAddress().getId());
		Address deliveryAddress = user.getDeliveryAddress();
		deliveryAddress.setId(toUpdate.getDeliveryAddress().getId());
		
		toUpdate.setBillingAddress(billingAddress);
		toUpdate.setDeliveryAddress(deliveryAddress);
		toUpdate.setEmail(user.getEmail());
		toUpdate.setLastname(user.getLastname());
		toUpdate.setFirstname(user.getFirstname());
		return userRepo.save(toUpdate);
	}

	public User editUserById(User user, Long id) {
		User toUpdate = userRepo.getReferenceById(id);

		Address billingAddress = user.getBillingAddress();
		billingAddress.setId(toUpdate.getBillingAddress().getId());
		Address deliveryAddress = user.getDeliveryAddress();
		deliveryAddress.setId(toUpdate.getDeliveryAddress().getId());

		toUpdate.setBillingAddress(billingAddress);
		toUpdate.setDeliveryAddress(deliveryAddress);
		toUpdate.setEmail(user.getEmail());
		toUpdate.setLastname(user.getLastname());
		toUpdate.setFirstname(user.getFirstname());
		return userRepo.save(toUpdate);
	}

}
