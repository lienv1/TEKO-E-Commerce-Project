package apiserver.apiserver.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import apiserver.apiserver.model.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UserSpecification implements Specification<User> {
	
	   private List<String> keywords;

	    public UserSpecification(List<String> keywords) {
	        this.keywords = keywords;
	    }

	    @Override
	    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
	        List<Predicate> predicates = new ArrayList<>();
	        if (keywords != null && !keywords.isEmpty()) {
	            for (String keyword : keywords) {
	                List<Predicate> keywordPredicates = new ArrayList<>();
	                // Add predicates for string fields
	                keywordPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + keyword.toLowerCase() + "%"));
	                keywordPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstname")), "%" + keyword.toLowerCase() + "%"));
	                keywordPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastname")), "%" + keyword.toLowerCase() + "%"));
	                keywordPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + keyword.toLowerCase() + "%"));
	                keywordPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + keyword.toLowerCase() + "%"));

	                // Handle Long fields
	                if (isNumeric(keyword)) {
	                    Long keywordAsLong = Long.parseLong(keyword);
	                    keywordPredicates.add(criteriaBuilder.equal(root.get("userId"), keywordAsLong));
	                    keywordPredicates.add(criteriaBuilder.equal(root.get("erpId"), keywordAsLong));
	                }

	                predicates.add(criteriaBuilder.or(keywordPredicates.toArray(new Predicate[0])));
	            }
	        }
	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	    }

    
	    public static boolean isNumeric(String str) {
	        if (str == null) {
	            return false;
	        }
	        int sz = str.length();
	        for (int i = 0; i < sz; i++) {
	            if (Character.isDigit(str.charAt(i)) == false) {
	                return false;
	            }
	        }
	        return true;
	    }

}
