package apiserver.apiserver.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import apiserver.apiserver.model.Favorite;
import jakarta.transaction.Transactional;

public interface FavoriteRepo extends JpaRepository<Favorite, Long>{
	
	@Modifying
	@Transactional
	void deleteByUser_UsernameAndProduct_ProductId(String username, Long productId);
	
	boolean existsByUserUsernameAndProductProductId(String username, Long productId);
	
}
