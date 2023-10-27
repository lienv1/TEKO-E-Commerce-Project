package apiserver.apiserver.dto;

import java.util.List;

public class CategoryListDTO {

	private String category;
	
	private List<String> brands;
	private List<String> subCategory;
	private List<String> origins;

	public CategoryListDTO() {
	}

	public String getCategory() {
		return category;
	}

	public List<String> getBrands() {
		return brands;
	}

	public List<String> getSubCategory() {
		return subCategory;
	}

	public List<String> getOrigins() {
		return origins;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setBrands(List<String> brands) {
		this.brands = brands;
	}

	public void setSubCategory(List<String> subCategory) {
		this.subCategory = subCategory;
	}

	public void setOrigins(List<String> origins) {
		this.origins = origins;
	}


}
