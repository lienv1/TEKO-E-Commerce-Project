package apiserver.apiserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import apiserver.apiserver.model.Favorite;
import jakarta.transaction.Transactional;

@Repository
public interface FavoriteRepo extends JpaRepository<Favorite, Long>{
	
	@Modifying
	@Transactional
	void deleteByUser_UsernameAndProduct_ProductId(String username, Long productId);
	
	boolean existsByUserUsernameAndProductProductId(String username, Long productId);
	
	@Modifying
	@Transactional
	@Query(
	    value = "INSERT INTO favorite (product_id, user_id) SELECT :productId, u.id FROM User u WHERE u.username = :username", 
	    nativeQuery = true
	)
	Favorite addFavoriteByUsernameAndProductId(@Param("username") String username, @Param("productId") Long productId);

	
}
