package apiserver.apiserver.controller;

import java.security.Principal;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import apiserver.apiserver.dto.CategoryListDTO;
import apiserver.apiserver.dto.FilterDTO;
import apiserver.apiserver.exception.ProductNotFoundException;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.ProductService;
import apiserver.apiserver.service.UserService;

@RestController
@RequestMapping("/product")
public class ProductController {

	private final int MAXITEM = 12;

	@Autowired
	private ProductService productService;

	@Autowired
	private AuthorizationService authorizationService;

	@Autowired
	private UserService userService;

	public ProductController(ProductService productService, AuthorizationService authorizationService,
			UserService userService) {
		this.productService = productService;
		this.authorizationService = authorizationService;
		this.userService = userService;
	}

	@GetMapping("/all")
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> allProducts = productService.getAllProducts();
		return new ResponseEntity<List<Product>>(allProducts, HttpStatus.OK);
	}

	// Experiment
	@GetMapping("/products")
	public ResponseEntity<Page<Product>> getProductsByParam(@PageableDefault(size = MAXITEM) Pageable pageable,
			@RequestParam(value = "brand", required = false) List<String> brands,
			@RequestParam(value = "origin", required = false) List<String> origins,
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "subcategory", required = false) String subCategory,
			@RequestParam(value = "keywords", required = false) List<String> keywords,
			@RequestParam(value = "favorite", required = false) String username) {

		// Manual handling of authorization
		if (username != null) {
			Principal principal = SecurityContextHolder.getContext().getAuthentication();
			if (!authorizationService.isAuthenticatedByPrincipal(principal, username))
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		Page<Product> list = productService.getProducts(pageable, origins, brands, category, subCategory, keywords,
				username);
		return new ResponseEntity<Page<Product>>(list, HttpStatus.OK);
	}

	@GetMapping("/filters")
	public ResponseEntity<FilterDTO> getFilters(
			@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "subcategory", required = false) String subCategory,
			@RequestParam(value = "keywords", required = false) List<String> keywords,
			@RequestParam(value = "favorite", required = false) String username) {
		
		if (username != null) {
			Principal principal = SecurityContextHolder.getContext().getAuthentication();
			if (!authorizationService.isAuthenticatedByPrincipal(principal, username))
				return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}

		FilterDTO filters = productService.getFilterDTO(category, subCategory, keywords, username);
		return new ResponseEntity<FilterDTO>(filters, HttpStatus.OK);
	}
	// Experiment end

	@GetMapping("/id/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
		try {
			Product product;
			product = productService.getProductById(id);
			return new ResponseEntity<Product>(product, HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Product> addProduct(@RequestBody Product product, Principal principal) {
		String username = principal.getName();
		if (!authorizationService.isAuthenticatedByPrincipal(principal, username))
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		Product newProduct = productService.addProduct(product);
		return new ResponseEntity<Product>(newProduct, HttpStatus.OK);
	}

	@PutMapping("/id/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Product> editProduct(@PathVariable("id") Long id, Product product, Principal principal) {

		boolean isAuthorized = authorizationService.isAuthenticatedByPrincipal(principal, principal.getName());
		if (!isAuthorized)
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);

		try {
			productService.getProductById(id);
			product.setProductId(id);
			Product updatedProduct = productService.editProduct(product);
			return new ResponseEntity<Product>(updatedProduct, HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			return new ResponseEntity("Product doesn't exist", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/category")
	public ResponseEntity<List<CategoryListDTO>> getCategoryList() {
		List<CategoryListDTO> categoryList = productService.getCategoryList();
		return new ResponseEntity<List<CategoryListDTO>>(categoryList, HttpStatus.OK);
	}

	@DeleteMapping("/products")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteAllProducts() {
		productService.deleteAllProducts();
		return ResponseEntity.ok().build();
	}

}
