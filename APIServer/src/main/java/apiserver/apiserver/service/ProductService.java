package apiserver.apiserver.service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import apiserver.apiserver.dto.CategoryListDTO;
import apiserver.apiserver.exception.ProductNotFoundException;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.repo.ProductRepo;
import apiserver.apiserver.specification.ProductSpecification;
import apiserver.apiserver.specification.ProductSpecifications;
import jakarta.persistence.Tuple;

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

	public Page<Product> getProducts(Pageable pageable) {
		return productRepo.findAll(pageable);
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

	public Page<Product> getProductsByFilters(List<String> brands, String category, String subCategory,
			List<String> origins, Pageable page) {

		Specification<Product> specification = Specification.where(null);

		if (brands != null && !brands.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasBrand(brands));
		}
		if (category != null && !category.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasCategory(category));
		}
		if (subCategory != null && !subCategory.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasSubCategory(subCategory));
		}
		if (origins != null && !origins.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasOrigin(origins));
		}

		return productRepo.findAll(specification, page);
	}

	public Page<Product> getProductsBySearch(List<String> keywords, Pageable page) {
		Specification<Product> specification = Specification.where(null);
		if (keywords != null && !keywords.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasSearchIndex(keywords));
		}
		return productRepo.findAll(specification, page);
	}

	public Set<CategoryListDTO> getCategoryList() {
		Set<Tuple> results = productRepo.getCategoryList();
		return results.stream().map(this::convertToDTO).collect(Collectors.toSet());
	}

	private CategoryListDTO convertToDTO(Tuple tuple) {
		CategoryListDTO dto = new CategoryListDTO();
		dto.setCategory(tuple.get("category", String.class));
		dto.setSubCategory(Arrays.asList(tuple.get("subCategory", String.class).split(",")));
		dto.setBrands(Arrays.asList(tuple.get("brands", String.class).split(",")));
		dto.setOrigins(Arrays.asList(tuple.get("origins", String.class).split(",")));
		return dto;
	}

}
