package apiserver.apiserver.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import apiserver.apiserver.model.Favorite;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.model.User;
import apiserver.apiserver.repo.FavoriteRepo;
import apiserver.apiserver.repo.ProductRepo;
import apiserver.apiserver.repo.UserRepo;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class FavoriteServiceTest {

	@Mock
	private ProductRepo productRepo;

	@Mock
	private UserRepo userRepo;

	@Mock
	private FavoriteRepo favoriteRepo;

	@InjectMocks
	private ProductService productService;

	@InjectMocks
	private UserService userService;

	@InjectMocks
	private FavoriteService favoriteService;

	private User user;

	private Product product;

	private Favorite favorite;

	private List<Favorite> favoriteList;

	@BeforeEach
	void init() {
		user = new User();
		user.setUsername("johndoe");

		product = new Product();
		product.setProductId(14326l);
		product.setProductName("Sriracha Hot Chili Sauce");

		favorite = new Favorite();
		favorite.setProduct(product);
		favorite.setUser(user);

		// Mock to set the ID when save is called
		when(userRepo.save(any(User.class))).thenAnswer(element -> {
			User savedUser = element.getArgument(0);
			savedUser.setUserId(1l);
			return savedUser;
		});
		// Mock to set the ID when save is called
		when(favoriteRepo.save(any(Favorite.class))).thenAnswer(element -> {
			Favorite savedFavorite = element.getArgument(0);
			savedFavorite.setFavouriteId(1l);
			return savedFavorite;
		});
		// ID is set manually, no need to mock ID when save
		when(productRepo.save(any(Product.class))).thenReturn(product);

		userRepo.save(user);
		productRepo.save(product);
		favoriteRepo.save(favorite);
	}

	@Test
	void addFavoriteTest() {

		Favorite addedFavorite = favoriteService.addFavorite(favorite);
		assertNotNull(addedFavorite.getFavouriteId());

		assertEquals(14326l, addedFavorite.getProduct().getProductId());
		assertEquals("Sriracha Hot Chili Sauce", addedFavorite.getProduct().getProductName());

		assertNotNull(addedFavorite.getUser().getUserId());
		assertEquals("johndoe", addedFavorite.getUser().getUsername());

	}
	
	@Test
	void getAllFavoriteTest() {
		favoriteList = new ArrayList<Favorite>();
		favoriteList.add(favorite);
		when(favoriteRepo.findAll()).thenReturn(favoriteList);
		List<Favorite> allFavorite = favoriteService.getAllFavorite();
		assertEquals(1, allFavorite.size());
	}
	
	
	void removeFavorite() {
		when(favoriteRepo.findAll()).thenReturn(new ArrayList<Favorite>());
		favoriteService.removeFavoriteByUsernameAndProductId(user.getUsername(), product.getProductId());
		List<Favorite> list = favoriteService.getAllFavorite();
		assertEquals(0, list.size());
	}

}
