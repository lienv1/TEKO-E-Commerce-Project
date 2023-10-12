package apiserver.apiserver.specification;

import org.springframework.data.jpa.domain.Specification;

import apiserver.apiserver.model.Product;
import jakarta.persistence.criteria.Predicate;

public class ProductSpecification {
	
	public Specification<Product> searchByCriteria(Product criteria){
		 return (root, query, builder) -> {
	            Predicate predicate = builder.conjunction();
	            
	            if (criteria.getProductName() != null) {
	                predicate = builder.and(predicate, builder.like(root.get("productName"), "%" + criteria.getProductName() + "%"));
	            }

	            if (criteria.getCategory() != null) {
	                predicate = builder.and(predicate, builder.equal(root.get("category"), criteria.getCategory()));
	            }

	            if (criteria.getPrice() != null) {
	                predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get("price"), criteria.getPrice()));
	            }

	            if (criteria.getBrand() != null) {
	                predicate = builder.and(predicate, builder.equal(root.get("brand"), criteria.getBrand()));
	            }

	            if (criteria.getWeight() != null) {
	                predicate = builder.and(predicate, builder.equal(root.get("weight"), criteria.getWeight()));
	            }

	            if (criteria.getProductGroup() != null) {
	                predicate = builder.and(predicate, builder.equal(root.get("group"), criteria.getProductGroup()));
	            }

	            if (criteria.getPack() != null) {
	                predicate = builder.and(predicate, builder.equal(root.get("pack"), criteria.getPack()));
	            }

	            if (criteria.getSearchIndex() != null) {
	                predicate = builder.and(predicate, builder.like(root.get("searchIndex"), "%" + criteria.getSearchIndex() + "%"));
	            }

	            if (criteria.getGtinUnit() != null) {
	                predicate = builder.and(predicate, builder.equal(root.get("gtinUnit"), criteria.getGtinUnit()));
	            }

	            if (criteria.getGtinPack() != null) {
	                predicate = builder.and(predicate, builder.equal(root.get("gtinPack"), criteria.getGtinPack()));
	            }

	            if (criteria.getStock() != null) {
	                predicate = builder.and(predicate, builder.equal(root.get("stock"), criteria.getStock()));
	            }

	            if (criteria.getOrigin() != null) {
	                predicate = builder.and(predicate, builder.equal(root.get("origin"), criteria.getOrigin()));
	            }

	            return predicate;

	        };
		
	}

}
