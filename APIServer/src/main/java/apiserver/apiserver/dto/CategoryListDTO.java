package apiserver.apiserver.dto;

import java.util.Set;

public class CategoryListDTO {

	private String category;
	
	private Set<String> subCategory;

	public CategoryListDTO() {
	}

	public String getCategory() {
		return category;
	}

	public Set<String> getSubCategory() {
		return subCategory;
	}

	
	public void setCategory(String category) {
		this.category = category;
	}

	public void setSubCategory(Set<String> subCategory) {
		this.subCategory = subCategory;
	}

}
