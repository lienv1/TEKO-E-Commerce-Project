package apiserver.apiserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import apiserver.apiserver.model.Favorite;

public interface FavoriteRepo extends JpaRepository<Favorite, Long>{
	
	
	
}
