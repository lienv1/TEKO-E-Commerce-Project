package apiserver.apiserver.dto;

import java.util.List;

public class CategoryListDTO {

	private List<String> brands;
	private List<String> categories;
	private List<String> productGroups;
	private List<String> origins;

	public CategoryListDTO() {
		super();
	}

	public List<String> getBrands() {
		return brands;
	}

	public List<String> getCategories() {
		return categories;
	}

	public List<String> getProductGroups() {
		return productGroups;
	}

	public List<String> getOrigins() {
		return origins;
	}

	public void setBrands(List<String> brands) {
		this.brands = brands;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public void setProductGroups(List<String> productGroups) {
		this.productGroups = productGroups;
	}

	public void setOrigins(List<String> origins) {
		this.origins = origins;
	}

}
