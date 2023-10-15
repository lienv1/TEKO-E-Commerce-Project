package apiserver.apiserver.repo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import apiserver.apiserver.model.Product;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RunWith(SpringRunner.class)
class ProductRepoTest {

	@Autowired
	private ProductRepo productRepo;

	private Product product;

	@BeforeEach
	void setUpProduct() {
		product = new Product();
		product.setProductId(14326l);
		product.setProductName("Sriracha Hot Chili Sauce");
		product.setBrand("Huy Fong Foods");
		product.setWeight(481);
		product.setCategory("Dry Goods");
		product.setProductGroup("Sauce");
		product.setPack(12);
		product.setGtinUnit("024463061095");
		product.setGtinPack("024463101098");
		product.setPrice(6.50);
		product.setStock(120);
		product.setOrigin("Thailand");
	}

	@Test
	void saveProductTest() {
		Product savedProduct = productRepo.save(product);

		Optional<Product> retrievedProductOptional = productRepo.findById(savedProduct.getProductId());

		// Make sure it's present
		assertTrue(retrievedProductOptional.isPresent());
		Product retrievedProduct = retrievedProductOptional.get();

		// Assertions to verify the product attributes
		assertNotNull(retrievedProduct.getProductId());
		assertEquals("Sriracha Hot Chili Sauce", retrievedProduct.getProductName());
		assertEquals("Huy Fong Foods", retrievedProduct.getBrand());
		assertEquals(481, retrievedProduct.getWeight());
		assertEquals("Dry Goods", retrievedProduct.getCategory());
		assertEquals("Sauce", retrievedProduct.getProductGroup());
		assertEquals(12, retrievedProduct.getPack());
		assertEquals("024463061095", retrievedProduct.getGtinUnit());
		assertEquals("024463101098", retrievedProduct.getGtinPack());
		assertEquals(6.50, retrievedProduct.getPrice());
		assertEquals(120, retrievedProduct.getStock());
		assertEquals("Thailand", retrievedProduct.getOrigin());
	}

	@Test
	void saveProductTestFail() {
		Product emptyProduct = new Product();
		JpaSystemException e = assertThrows(JpaSystemException.class, () -> {
			productRepo.save(emptyProduct);
		});
	}

}
