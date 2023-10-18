package apiserver.apiserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import apiserver.apiserver.model.Order;

public interface OrderRepo extends JpaRepository<Order, Long>{

	List<Order> findByUserUsername(String username);
	
}
