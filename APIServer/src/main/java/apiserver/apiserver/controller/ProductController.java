package apiserver.apiserver.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		if (!authorizationService.isAuthenticatedByPrincipal(principal,username))
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		Product newProduct = productService.addProduct(product);
		return new ResponseEntity<Product>(newProduct,HttpStatus.OK);
	}
	
	@PutMapping("/edit/id/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Product> editProduct(@PathVariable("id") Long id ,Product product){
		try {
			productService.getProductById(id);
			product.setProductId(id);
			Product updatedProduct = productService.editProduct(product);
			return new ResponseEntity<Product>(updatedProduct,HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/id/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id){
		try {
			Product toDelete = productService.deleteProduct(id);
			return new ResponseEntity<Product>(toDelete,HttpStatus.OK);
		} catch (ProductNotFoundException e) {
			return new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
		}
	}
	
	
}
