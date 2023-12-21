package apiserver.apiserver.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import apiserver.apiserver.model.User;

public class UserSpecification {

	 public static Specification<User> userIdContains(String keyword) {
	        return (root, query, criteriaBuilder) -> {
	            if (keyword == null || keyword.isEmpty() || isNumeric(keyword)) {
	                return criteriaBuilder.conjunction();
	            }
	            return criteriaBuilder.like(criteriaBuilder.lower(root.get("userId")), "%" + keyword.toLowerCase() + "%");
	        };
	    }
	
	 public static Specification<User> usernameContains(String keyword) {
	        return (root, query, criteriaBuilder) -> {
	            if (keyword == null || keyword.isEmpty()) {
	                return criteriaBuilder.conjunction();
	            }
	            return criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + keyword.toLowerCase() + "%");
	        };
	    }

	    public static Specification<User> erpIdContains(String keyword) {
	        return (root, query, criteriaBuilder) -> {
	            if (keyword == null || keyword.isEmpty() || isNumeric(keyword)) {
	                return criteriaBuilder.conjunction();
	            }
	            return criteriaBuilder.like(criteriaBuilder.lower(root.get("erpId")), "%" + keyword.toLowerCase() + "%");
	        };
	    }
	    
	    public static Specification<User> firstnameContains(String keyword) {
	        return (root, query, criteriaBuilder) -> {
	            if (keyword == null || keyword.isEmpty()) {
	                return criteriaBuilder.conjunction();
	            }
	            return criteriaBuilder.like(criteriaBuilder.lower(root.get("firstname")), "%" + keyword.toLowerCase() + "%");
	        };
	    }
	    
	    
	    public static Specification<User> lastnameContains(String keyword) {
	        return (root, query, criteriaBuilder) -> {
	            if (keyword == null || keyword.isEmpty()) {
	                return criteriaBuilder.conjunction();
	            }
	            return criteriaBuilder.like(criteriaBuilder.lower(root.get("lastname")), "%" + keyword.toLowerCase() + "%");
	        };
	    }
	    
	    public static Specification<User> emailContains(String keyword) {
	        return (root, query, criteriaBuilder) -> {
	            if (keyword == null || keyword.isEmpty()) {
	                return criteriaBuilder.conjunction();
	            }
	            return criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + keyword.toLowerCase() + "%");
	        };
	    }
	    
	    public static Specification<User> phoneContains(String keyword) {
	        return (root, query, criteriaBuilder) -> {
	            if (keyword == null || keyword.isEmpty()) {
	                return criteriaBuilder.conjunction();
	            }
	            return criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + keyword.toLowerCase() + "%");
	        };
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
