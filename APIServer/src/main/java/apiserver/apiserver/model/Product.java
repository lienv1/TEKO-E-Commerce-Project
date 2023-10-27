package apiserver.apiserver.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {

	@Id
	@Column(nullable = false)
	private Long productId;

	private String productName;
	private String description;
	private String brand;
	private Integer weight;
	private String category;
	private String subCategory;
	private Integer pack;
	private String searchIndex;
	private String gtinUnit;
	private String gtinPack;
	private Double price;
	private Integer stock;
	private String origin;
	private Boolean discount;
	private Double tax;
	private Date lastModified;
	private boolean deleted;

	public Product() {
	}

	public Long getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public String getDescription() {
		return description;
	}

	public String getBrand() {
		return brand;
	}

	public Integer getWeight() {
		return weight;
	}

	public String getCategory() {
		return category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public Integer getPack() {
		return pack;
	}

	public String getSearchIndex() {
		return searchIndex;
	}

	public String getGtinUnit() {
		return gtinUnit;
	}

	public String getGtinPack() {
		return gtinPack;
	}

	public Double getPrice() {
		return price;
	}

	public Integer getStock() {
		return stock;
	}

	public String getOrigin() {
		return origin;
	}

	public Boolean getDiscount() {
		return discount;
	}

	public Double getTax() {
		return tax;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public void setPack(Integer pack) {
		this.pack = pack;
	}

	public void setSearchIndex(String searchIndex) {
		this.searchIndex = searchIndex;
	}

	public void setGtinUnit(String gtinUnit) {
		this.gtinUnit = gtinUnit;
	}

	public void setGtinPack(String gtinPack) {
		this.gtinPack = gtinPack;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public void setDiscount(Boolean discount) {
		this.discount = discount;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}


	
}