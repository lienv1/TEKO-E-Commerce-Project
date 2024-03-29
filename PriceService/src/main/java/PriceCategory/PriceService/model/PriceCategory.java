package PriceCategory.PriceService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PriceCategory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String product;
	private Integer productType;
	private Integer customer;
	private Integer customerType;
	private Double value;
	private Boolean percent;
	private Boolean discount;
	private Double quantity;
	private String priceTyp;
	private Integer priority;
	
	public PriceCategory() {
		
	}

	public long getId() {
		return id;
	}

	public String getProduct() {
		return product;
	}

	public Integer getProductType() {
		return productType;
	}

	public Integer getCustomer() {
		return customer;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public Double getValue() {
		return value;
	}

	public Boolean getPercent() {
		return percent;
	}

	public Boolean getDiscount() {
		return discount;
	}

	public Double getQuantity() {
		return quantity;
	}

	public String getPriceTyp() {
		return priceTyp;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public void setCustomer(Integer customer) {
		this.customer = customer;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public void setPercent(Boolean percent) {
		this.percent = percent;
	}

	public void setDiscount(Boolean discount) {
		this.discount = discount;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public void setPriceTyp(String priceTyp) {
		this.priceTyp = priceTyp;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	

}
