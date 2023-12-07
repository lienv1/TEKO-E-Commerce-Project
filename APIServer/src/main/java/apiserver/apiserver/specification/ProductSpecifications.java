package apiserver.apiserver.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import apiserver.apiserver.model.Product;
import jakarta.persistence.criteria.Predicate;

public class ProductSpecifications {
	
	  public static Specification<Product> isNotDeleted() {
	        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("deleted"));
	    }

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
			return criteriaBuilder.equal(root.get("subCategory"), subCategory);
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
	
	public static Specification<Product> hasSearchIndex(List<String> keywords){
		 return (root, query, criteriaBuilder) -> {
	            if (keywords == null || keywords.isEmpty()) {
	                return criteriaBuilder.conjunction(); // No condition
	            }
	            // Convert the list of keywords into a list of lowercase predicates
	            List<Predicate> predicates = new ArrayList<>();
	            for (String keyword : keywords) {
	                predicates.add(criteriaBuilder.like(
	                        criteriaBuilder.lower(root.get("searchIndex")),
	                        "%" + keyword.toLowerCase() + "%"
	                ));
	            }
	            // Combine the predicates with OR conditions
	            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	        };
	}
}
