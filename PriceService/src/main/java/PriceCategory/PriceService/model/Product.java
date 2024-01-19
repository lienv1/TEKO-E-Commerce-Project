package PriceCategory.PriceService.model;

public class Product {
	
	private Long productId;
	
	private String category;
	private String subCategory;
	private Double price;
	private Boolean discount;
	
	public Product() {}

	public Long getProductId() {
		return productId;
	}

	public String getCategory() {
		return category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public Double getPrice() {
		return price;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Boolean getDiscount() {
		return discount;
	}

	public void setDiscount(Boolean discount) {
		this.discount = discount;
	}

	

}
