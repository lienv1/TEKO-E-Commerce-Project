package apiserver.apiserver.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import apiserver.apiserver.exception.ProductNotFoundException;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.repo.ProductRepo;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ProductServiceTest {
	
	@Mock
	private ProductRepo productRepo;
	
	@InjectMocks
	private ProductService productService;
	
	private Product product;
	
	private List<Product> productList;
	
	@BeforeEach
	void setUpProduct() {
		product = new Product();
		product.setProductId(14326l);
		product.setProductName("Sriracha Hot Chili Sauce");
		product.setBrand("Huy Fong Foods");
		product.setWeight(481);
		product.setCategory("Dry Goods");
		product.setSubCategory("Sauce");
		product.setPack(12);
		product.setGtinUnit("024463061095");
		product.setGtinPack("024463101098");
		product.setPrice(6.50);
		product.setStock(120);
		product.setOrigin("Thailand");
		
		productList = new ArrayList<Product>();
		productList.add(product);

	}

	@Test
	void getAllProductTest() {
		when(productRepo.findAll()).thenReturn(productList);
		productService.addProduct(product);
		List<Product> list = productService.getAllProducts();
		assertEquals(1,list.size());
		assertEquals(14326l,list.get(0).getProductId());
		assertEquals("Sriracha Hot Chili Sauce", list.get(0).getProductName());
	}
	
	@Test
	void getProductByIdTest() {
		when(productRepo.save(any(Product.class))).thenReturn(product);
		productService.addProduct(product);
		when(productRepo.findById(product.getProductId())).thenReturn(Optional.of(product));
		Optional<Product> toRetrieve = productRepo.findById((product.getProductId()));
		assertTrue(toRetrieve.isPresent());
		assertEquals(product.getProductId(), toRetrieve.get().getProductId());
		assertEquals(product.getProductName(), toRetrieve.get().getProductName());
	}
	
	@Test
	void productNotFoundExceptionTest() {
		when(productRepo.findById(anyLong())).thenReturn(Optional.empty());
		ProductNotFoundException ex = assertThrows(ProductNotFoundException.class, () -> {
			productService.getProductById(14326l);
		});
		assertEquals("Product doesn't exist", ex.getMessage());
	}
	
	@Test
	void editProductTest() throws ProductNotFoundException {
	    double newPrice = 6.60;
	    product.setPrice(newPrice);
	    when(productRepo.existsById(product.getProductId())).thenReturn(true);
	    when(productRepo.save(any(Product.class))).thenReturn(product);
	    Product updatedProduct = productService.editProduct(product);
	    assertEquals(newPrice, updatedProduct.getPrice());
	}
	
	@Test
	void getProductByFilterTest() {
		Product filter = new Product();
		filter.setBrand(product.getProductName());
		when(productRepo.findAll(any(Specification.class))).thenReturn(productList);
		List<Product> products = productService.getProductsByFilter(filter);
		assertEquals(1, products.size());
	}

}
