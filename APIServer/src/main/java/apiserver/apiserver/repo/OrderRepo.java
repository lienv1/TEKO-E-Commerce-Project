package apiserver.apiserver.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import apiserver.apiserver.model.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long>{

	Page<Order> findByUserUsername(String username, Pageable pageable);
	
	Page<Order> findByUserUserId(long userid, Pageable pageable);
	
	@Query("SELECT COUNT(o) FROM Order o WHERE o.created > CURRENT_DATE AND o.user.userId = :userId")
    Long countRecentOrdersByUserId(Long userId);
	
}
