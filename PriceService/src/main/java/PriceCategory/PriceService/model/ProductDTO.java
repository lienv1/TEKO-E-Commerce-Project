package PriceCategory.PriceService.model;

public class ProductDTO {
	
	private Product product;
	private double quantity;
	
	public ProductDTO() {
		
	}

	public Product getProduct() {
		return product;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	
}
