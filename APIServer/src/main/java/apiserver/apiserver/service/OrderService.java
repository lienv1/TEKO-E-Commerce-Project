package apiserver.apiserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import apiserver.apiserver.exception.OrderNotFoundException;
import apiserver.apiserver.exception.UserNotFoundException;
import apiserver.apiserver.model.Order;
import apiserver.apiserver.repo.OrderRepo;

@Service
public class OrderService {

	@Autowired
	private OrderRepo orderRepo;

	public OrderService(OrderRepo orderRepo) {
		this.orderRepo = orderRepo;
	}

	public Order getOrderById(Long id) throws OrderNotFoundException {
		return orderRepo.findById(id).orElseThrow(() -> new OrderNotFoundException("Order not found"));
	}

	public Order addOrder(Order order) throws DataIntegrityViolationException {
		try {
			return orderRepo.save(order);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException(e.getMessage());
		}
	}

	public List<Order> getAllOrdersByUsername(String username) {
		return orderRepo.findByUserUsername(username);
	}

}
