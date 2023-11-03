package apiserver.apiserver.controller;

import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import apiserver.apiserver.dto.CategoryListDTO;
import apiserver.apiserver.exception.ProductNotFoundException;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.security.AuthorizationService;
import apiserver.apiserver.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private AuthorizationService authorizationService;
	
	
	public ProductController(ProductService productService, AuthorizationService authorizationService) {
		this.productService = productService;
		this.authorizationService = authorizationService;
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Product>> getAllProducts(){
		List<Product> allProducts = productService.getAllProducts();
		return new ResponseEntity<List<Product>>(allProducts,HttpStatus.OK);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable("id") Long id){
		try {
			Product product;
			product = productService.getProductById(id);
			return new ResponseEntity<Product>(product,HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Product> addProduct(@RequestBody Product product, Principal principal){
		String username = principal.getName();
		System.out.println(username);
		if (!authorizationService.isAuthenticatedByPrincipal(principal,username))
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		Product newProduct = productService.addProduct(product);
		return new ResponseEntity<Product>(newProduct,HttpStatus.OK);
	}
	
	@PutMapping("/id/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Product> editProduct(@PathVariable("id") Long id ,Product product, Principal principal){
		
		boolean isAuthorized = authorizationService.isAuthenticatedByPrincipal(principal, principal.getName());
		if (!isAuthorized) 
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		
		
		try {
			productService.getProductById(id);
			product.setProductId(id);
			Product updatedProduct = productService.editProduct(product);
			return new ResponseEntity<Product>(updatedProduct,HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			return new ResponseEntity("Product doesn't exist", HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/id/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id, Principal principal){
		
		boolean isAuthorized = authorizationService.isAuthenticatedByPrincipal(principal, principal.getName());
		if (!isAuthorized)
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		try {
			Product toDelete = productService.deleteProduct(id);
			return new ResponseEntity<Product>(toDelete,HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Product>> getProductsByFilter(@RequestBody Product filter){
		List<Product> list = productService.getProductsByFilter(filter);
		return new ResponseEntity<List<Product>>(list,HttpStatus.OK);
	}
	
	@GetMapping("/category")
	public ResponseEntity <List<CategoryListDTO>> getCategoryList(){
		List<CategoryListDTO>categoryList = productService.getCategoryList();
		return new ResponseEntity<List<CategoryListDTO>>(categoryList,HttpStatus.OK);
	}
	
	
}
