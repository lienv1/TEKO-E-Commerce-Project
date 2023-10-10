package apiserver.apiserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apiserver.apiserver.exception.ProductNotFoundException;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.repo.ProductRepo;

@Service
public class ProductService {
	
	@Autowired
	public ProductRepo productRepo;

	public ProductService(ProductRepo productRepo) {
		this.productRepo = productRepo;
	}
	
	public Product getProductBy(Long id) throws ProductNotFoundException {
		return productRepo.findById(id).orElseThrow( () -> new ProductNotFoundException("Product doesn't exist") ) ;
	}
	
	public Product addProduct(Product product) {
		return productRepo.save(product);
	}
	
	public List<Product> getProductsByFilter() {
		//TODO
		return null;
	}
	
	
	
	
}
