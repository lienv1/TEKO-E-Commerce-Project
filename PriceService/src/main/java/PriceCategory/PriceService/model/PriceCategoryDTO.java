package PriceCategory.PriceService.model;

public class PriceCategoryDTO {
	
	private Long productId;
	
	private Double price;
	
	public PriceCategoryDTO() {
		
	}

	public Long getProductId() {
		return productId;
	}

	public Double getPrice() {
		return price;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
