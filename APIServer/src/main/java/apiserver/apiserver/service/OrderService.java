package apiserver.apiserver.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import apiserver.apiserver.exception.OrderNotFoundException;
import apiserver.apiserver.exception.UserNotFoundException;
import apiserver.apiserver.model.Order;
import apiserver.apiserver.model.OrderDetail;
import apiserver.apiserver.model.User;
import apiserver.apiserver.repo.OrderRepo;
import apiserver.apiserver.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class OrderService {

	@Autowired
	private OrderRepo orderRepo;
	
	@Autowired
	private UserRepo userRepo;

	public OrderService(OrderRepo orderRepo, UserRepo userRepo) {
		this.orderRepo = orderRepo;
		this.userRepo = userRepo;
	}

	public Order getOrderById(Long id) throws OrderNotFoundException {
		return orderRepo.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));
	}

	@Transactional
	public Order addOrder(Order order) throws DataIntegrityViolationException, EntityNotFoundException {
		try {
			User user = order.getUser();
			//Check if user exist in database
			Optional<User> oldUser = userRepo.findByUsername(user.getUsername());
			if (oldUser.isPresent()) {
				Long erpId = oldUser.get().getErpId();
				if (erpId != null)
					user.setErpId(erpId);
			}
			user = userRepo.saveAndFlush(order.getUser());
			order.setUser(user);
			for (OrderDetail orderDetail: order.getOrderDetails()) {
				orderDetail.setOrder(order);
			}
			return orderRepo.save(order);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException(e.getMessage());
		}
		catch (EntityNotFoundException e) {
			throw new EntityNotFoundException(e.getMessage());
		}
	}

	public Page<Order> getAllOrdersByUsername(String username, Pageable page) {
		return orderRepo.findByUserUsername(username,page);
	}
	
	public Page<Order> getAllOrdersByUserid(long userid, Pageable page){
		return orderRepo.findByUserUserId(userid, page);
	}

}
