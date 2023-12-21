package apiserver.apiserver.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import apiserver.apiserver.exception.UserNotFoundException;
import apiserver.apiserver.model.Address;
import apiserver.apiserver.model.User;
import apiserver.apiserver.repo.UserRepo;
import apiserver.apiserver.specification.UserSpecification;

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

	public Set<String> getAllUsername() {
		return userRepo.findAllUsernames();
	}

	public Set<String> getAllErpId() {
		return userRepo.findAllErpId();
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

	public Set<User> getUsersByKeyword(String keyword) {
		return userRepo.findByUsernameContainingIgnoreCase(keyword);
	}

	public Set<User> getUsersByKeywords(List<String> keywords) {
		 if (keywords == null || keywords.isEmpty()) {
		        return new HashSet<>(userRepo.findAll());
		    }

		    Specification<User> combinedSpec = Specification.where(null);
		    for (String keyword : keywords) {
		        combinedSpec = combinedSpec
		        		.or(UserSpecification.userIdContains(keyword))
		        		.or(UserSpecification.usernameContains(keyword))
		                .or(UserSpecification.erpIdContains(keyword))
		                .or(UserSpecification.firstnameContains(keyword))
		                .or(UserSpecification.lastnameContains(keyword))
		                .or(UserSpecification.emailContains(keyword))
		                .or(UserSpecification.phoneContains(keyword))
		                ;
		    }
		    return new HashSet<>(userRepo.findAll(combinedSpec));
	}

	public User getUserByErpId(Long erpId) throws UserNotFoundException {
		return userRepo.findByErpId(erpId).orElseThrow(() -> new UserNotFoundException("User not found"));
	}

	public User editUserByUsername(User newUser, String username) throws UserNotFoundException {
		User oldUser = userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
		User updatedUser = editUser(newUser, oldUser);
		return updatedUser;
	}

	public User editUserById(User newUser, Long id) {
		User oldUser = userRepo.getReferenceById(id);
		User updatedUser = editUser(newUser, oldUser);
		return updatedUser;
	}

	public User editUser(User newUser, User oldUser) {

		Address billingAddress = newUser.getBillingAddress();
		Address deliveryAddress = newUser.getDeliveryAddress();
		if (billingAddress != null)
			oldUser.setBillingAddress(billingAddress);
		if (deliveryAddress != null)
			oldUser.setDeliveryAddress(deliveryAddress);

		oldUser.setEmail(newUser.getEmail());
		oldUser.setLastname(newUser.getLastname());
		oldUser.setFirstname(newUser.getFirstname());
		oldUser.setPhone(newUser.getPhone());
		return userRepo.save(oldUser);
	}

	public User deleteUserByUsername(String username) throws UserNotFoundException {
		User user = userRepo.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found"));
		user.setDeleted(true);
		return userRepo.save(user);
	}

	public boolean userExistsByUsername(String username) {
		return userRepo.existsByUsername(username);
	}

	public boolean userExistByUserId(long userid) {
		return userRepo.existsById(userid);
	}

}
