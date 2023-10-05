package apiserver.apiserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import apiserver.apiserver.model.User;
import apiserver.apiserver.repo.UserRepo;

@Service
public class UserService {

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
	
	public User editUser(User user, Long id) {
		User toUpdate = userRepo.getReferenceById(id);
		toUpdate.setBillingAddress(user.getBillingAddress());
		toUpdate.setDeliveryAddress(user.getDeliveryAddress());
		toUpdate.setEmail(user.getEmail());
		toUpdate.setLastname(user.getLastname());
		toUpdate.setFirstname(user.getFirstname());
		return userRepo.save(toUpdate);
	}

}
