package apiserver.apiserver.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import apiserver.apiserver.model.Product;

public class ProductSpecifications {

	public static Specification<Product> hasBrand(List<String> brands) {
		return (root, query, criteriaBuilder) -> {
			if (brands == null || brands.isEmpty()) {
				return criteriaBuilder.conjunction(); // No condition
			}
			return root.get("brand").in(brands); // WHERE brand IN (...)
		};
	}

	public static Specification<Product> hasCategory(String category) {
		return (root, query, criteriaBuilder) -> {
			if (category == null || category.isEmpty()) {
				return criteriaBuilder.conjunction(); // No condition
			}
			return criteriaBuilder.equal(root.get("category"), category); // WHERE category = :category
		};
	}

	public static Specification<Product> hasSubCategory(String subCategory) {
		return (root, query, criteriaBuilder) -> {
			if (subCategory == null || subCategory.isEmpty()) {
				return criteriaBuilder.conjunction(); // No condition
			}
			return criteriaBuilder.equal(root.get("sub_category"), subCategory);
		};
	}

	public static Specification<Product> hasOrigin(List<String> origins) {
		return (root, query, criteriaBuilder) -> {
			if (origins == null || origins.isEmpty()) {
				return criteriaBuilder.conjunction(); // No condition
			}
			return root.get("origin").in(origins); // WHERE origin IN (...)
		};
	}
}
