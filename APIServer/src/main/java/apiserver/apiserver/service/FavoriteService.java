package apiserver.apiserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apiserver.apiserver.model.Favorite;
import apiserver.apiserver.repo.FavoriteRepo;

@Service
public class FavoriteService {
	
	@Autowired
	private FavoriteRepo favoriteRepo;
	
	public FavoriteService(FavoriteRepo favoriteRepo) {
		this.favoriteRepo = favoriteRepo;
	}

	public Favorite addFavorite(Favorite favorite) {
		return favoriteRepo.save(favorite);
	}
	
	public Favorite addFavoriteByUsernameAndProductId(String username, Long id) {
		return favoriteRepo.addFavoriteByUsernameAndProductId(username, id);
	}
	
	public List<Favorite> getAllFavorite(){
		return favoriteRepo.findAll();
	}

	public void removeFavoriteByUsernameAndProductId(String username, Long productId) {
		favoriteRepo.deleteByUser_UsernameAndProduct_ProductId(username, productId);
	}
	
	public boolean isFavorite(String username, Long productId) {
		return favoriteRepo.existsByUserUsernameAndProductProductId(username, productId);
	}

}
