package apiserver.apiserver.service;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import apiserver.apiserver.dto.CategoryListDTO;
import apiserver.apiserver.dto.FilterDTO;
import apiserver.apiserver.dto.ProductBrandDTO;
import apiserver.apiserver.dto.ProductOriginDTO;
import apiserver.apiserver.exception.ProductNotFoundException;
import apiserver.apiserver.model.Product;
import apiserver.apiserver.repo.ProductRepo;
import apiserver.apiserver.specification.ProductSpecifications;

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
		return productRepo.findAllActiveProducts(pageable);
	}

	public Page<Product> getProducts2(Pageable pageable, List<String> origins, List<String> brands, String category, String subCategory, List<String> keywords, String username) {
		Specification<Product> specification = Specification.where(null);
		specification = specification.and(ProductSpecifications.isNotDeleted());
		if (origins != null && !origins.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasOrigin(origins));
		}
		if (brands != null && !brands.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasBrand(brands));
		}
		if (category != null && !category.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasCategory(category));
		}
		if (subCategory != null && !subCategory.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasSubCategory(subCategory));
		}
		if (keywords != null && !keywords.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasSearchIndex(keywords));		
		}
		if (username != null && !username.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasFavoriteForUser(username));		
		}
		return productRepo.findAll(specification,pageable);
	}
	
	public FilterDTO getFilterDTO2(String category, String subCategory, List<String> keywords, String username){
		Specification<Product> specification = Specification.where(null);
		specification = specification.and(ProductSpecifications.isNotDeleted());
		if (category != null && !category.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasCategory(category));
		}
		if (subCategory != null && !subCategory.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasSubCategory(subCategory));
		}
		if (keywords != null && !keywords.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasSearchIndex(keywords));		
		}
		if (username != null && !username.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasFavoriteForUser(username));		
		}
		
		List<ProductBrandDTO> listOfBrands = productRepo.findBy(specification, q -> q.as(ProductBrandDTO.class).all());
		List<ProductOriginDTO> listOfOrigins = productRepo.findBy(specification, q -> q.as(ProductOriginDTO.class).all());
		
		FilterDTO filter = new FilterDTO();
		
		filter.setBrands(new HashSet<String>(listOfBrands.stream().map(element -> element.getBrand()).toList()));
		filter.setOrigins(new HashSet<String>(listOfOrigins.stream().map(element -> element.getOrigin()).toList()));
		
		return filter;
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

	/*public Product deleteProduct(Long id) throws ProductNotFoundException {
		boolean isPresent = productRepo.existsById(id);
		if (!isPresent)
			throw new ProductNotFoundException("");
		return productRepo.updateDeletedStatus(id, true);
	}*/

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
		specification = specification.and(ProductSpecifications.isNotDeleted());

		return productRepo.findAll(specification, page);
	}

	public Page<Product> getProductsBySearch(List<String> keywords, Pageable page, List<String> brands, List<String> origins ) {
		Specification<Product> specification = Specification.where(null);
		if (keywords != null && !keywords.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasSearchIndex(keywords));		
		}
		if (brands != null && !brands.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasBrand(brands));
		}
		if (origins != null && !origins.isEmpty()) {
			specification = specification.and(ProductSpecifications.hasOrigin(origins));
		}
		specification = specification.and(ProductSpecifications.isNotDeleted());
		return productRepo.findAll(specification, page);
	}

	public Page<Product> getProductsByFavorite(String username, Pageable page, List<String> brands,
			List<String> origins) {
		return productRepo.findFavoriteProductsByUserUsername(username, page, brands, origins);
	}

	public List<CategoryListDTO> getCategoryList() {
		List<String[]> results = productRepo.findCategoriesAndSubcategories();
		Map<String, Set<String>> categoryToSubCategories = new LinkedHashMap<>();

		for (Object[] result : results) {
			String category = (String) result[0];
			String subCategory = (String) result[1];

			// Initialize a new Set for this category if one does not already exist
			categoryToSubCategories.computeIfAbsent(category, k -> new HashSet<>());

			// Add the subcategory to the Set for this category
			categoryToSubCategories.get(category).add(subCategory);
		}

		// Now we can create our list of CategoryListDTOs
		List<CategoryListDTO> categoryListDTOs = categoryToSubCategories.entrySet().stream().map(entry -> {
			CategoryListDTO dto = new CategoryListDTO();
			dto.setCategory(entry.getKey());
			dto.setSubCategory(entry.getValue());
			return dto;
		}).collect(Collectors.toList());
		return categoryListDTOs;
	}

	public FilterDTO getFiltersByCategory(List<String> category, List<String> subCategory) {
		FilterDTO filter = new FilterDTO();
		Set<String> brands = productRepo.findDistinctBrands(category, subCategory);
		Set<String> origins = productRepo.findDistinctOrigins(category, subCategory);
		filter.setBrands(brands);
		filter.setOrigins(origins);
		return filter;
	}

	public FilterDTO getFiltersBySearch(List<String> keywords) {
		FilterDTO filter = new FilterDTO();
		Specification<Product> spec = Specification.where(null);
		spec = spec.and(ProductSpecifications.hasSearchIndex(keywords));
		List<Product> products = productRepo.findAll(spec);
		Set<String> brands = new HashSet<String>(products.stream().map(element -> element.getBrand()).toList());
		Set<String> origins = new HashSet<String>(products.stream().map(element -> element.getOrigin()).toList());
		filter.setBrands(brands);
		filter.setOrigins(origins);
		return filter;
	}

	public FilterDTO getFiltersByFavorite(String username) {
		FilterDTO filter = new FilterDTO();
		Set<String> brands = productRepo.findFavoriteBrands(username);
		Set<String> origins = productRepo.findFavoriteOrigins(username);
		filter.setBrands(brands);
		filter.setOrigins(origins);
		return filter;
	}
	
	public void deleteAllProducts() {
		productRepo.updateAllDeletedStatus(true);
	}

}
