package apiserver.apiserver.repo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import apiserver.apiserver.model.Favorite;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FavoriteRepoTest {

	@Autowired
	private FavoriteRepo favoriteRepo;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private UserRepo userRepo;

	private Favorite favorite;
	private User user;
	private Product product;

	@BeforeEach
	void setup() {
		user = new User();
		user.setUserId(1000l);
		user.setUsername("johndoe");
		userRepo.save(user);

		product = new Product();
		product.setProductId(14326l);
		product.setProductName("Sriracha Hot Chili Sauce");
		productRepo.save(product);

		favorite = new Favorite();
		favorite.setProduct(product);
		favorite.setUser(user);
		Favorite favorite = favoriteRepo.save(this.favorite);
	}

	@Test
	void saveTest() {

		assertNotNull(favorite);
		assertNotNull(favorite.getFavouriteId());

		assertEquals(14326l, favorite.getProduct().getProductId());
		assertEquals("Sriracha Hot Chili Sauce", favorite.getProduct().getProductName());

		assertNotNull(favorite.getUser().getUserId());
		assertEquals("johndoe", favorite.getUser().getUsername());
	}

	@Test
	void saveTestFail() {
		
		// Attempt to save a duplicate favorite (same user and product)
		Favorite duplicateFavorite = new Favorite();
		duplicateFavorite.setProduct(product);
		duplicateFavorite.setUser(user);

		// Assert that an exception is thrown when trying to save the duplicate
		assertThrows(DataIntegrityViolationException.class, () -> {
			favoriteRepo.save(duplicateFavorite);
		});
	}

}
