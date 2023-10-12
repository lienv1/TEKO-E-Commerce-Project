package apiserver.apiserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import apiserver.apiserver.exception.ProductNotFoundException;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.repo.ProductRepo;
import apiserver.apiserver.specification.ProductSpecification;

@Service
public class ProductService {
	
	@Autowired
	public ProductRepo productRepo;

	public ProductService(ProductRepo productRepo) {
		this.productRepo = productRepo;
	}
	
	public List<Product> getAllProducts(){
		return productRepo.findAll();
	}
	
	public Product getProductBy(Long id) throws ProductNotFoundException {
		return productRepo.findById(id).orElseThrow( () -> new ProductNotFoundException("Product doesn't exist") ) ;
	}
	
	public Product addProduct(Product product) {
		return productRepo.save(product);
	}
	
	public Product editProduct(Product product) {
		return productRepo.save(product);
	}
	
	public Product deleteProduct(Long id) throws ProductNotFoundException {
		Product productToDelete = productRepo.findById(id).orElseThrow( () -> new ProductNotFoundException("Can not find product with the id " + id ));
		productToDelete.setDeleted(true);
		return productRepo.save(productToDelete);
	}
	
	public List<Product> getProductsByFilter(Product criteria) {
		Specification<Product> specification = new ProductSpecification().searchByCriteria(criteria);
		return productRepo.findAll(specification);
	}
	
	
	
	
}
