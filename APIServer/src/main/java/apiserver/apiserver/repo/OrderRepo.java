package apiserver.apiserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import apiserver.apiserver.model.Order;

public interface OrderRepo extends JpaRepository<Order, Long>{

}
