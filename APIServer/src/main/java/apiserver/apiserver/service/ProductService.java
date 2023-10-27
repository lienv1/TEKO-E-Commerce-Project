package apiserver.apiserver.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import apiserver.apiserver.dto.CategoryListDTO;
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

	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}

	public Product getProductById(Long id) throws ProductNotFoundException {
		return productRepo.findById(id).orElseThrow(() -> new ProductNotFoundException("Product doesn't exist"));
	}

	public Product addProduct(Product product) {
		return productRepo.save(product);
	}

	public Product editProduct(Product product) throws ProductNotFoundException {
		if (!productRepo.existsById(product.getProductId())) {
			throw new ProductNotFoundException("Product doesn't exist");
		}
		return productRepo.save(product);
	}

	public Product deleteProduct(Long id) throws ProductNotFoundException {
		boolean isPresent = productRepo.existsById(id);
		if (!isPresent)
			throw new ProductNotFoundException("");
		return productRepo.updateDeletedStatus(id, true);
	}

	public List<Product> getProductsByFilter(Product criteria) {
		Specification<Product> specification = new ProductSpecification().searchByCriteria(criteria);
		return productRepo.findAll(specification);
	}

	public List<CategoryListDTO> getCategoryList() {
		return productRepo.getCategoryList();
	}

}
