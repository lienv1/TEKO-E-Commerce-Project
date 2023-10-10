package apiserver.apiserver.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;

	private String productName;
	private String description;
	private String brand;
	private int weight;
	private String category;
	private String group;
	private int pack;
	private String searchIndex;
	private String gtinUnit;
	private String gtinPack;
	private double price;
	private int stock;
	private String origin;
	private boolean discount;
	private int taxCode;
	private Date lastModified;

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

	public int getWeight() {
		return weight;
	}

	public String getCategory() {
		return category;
	}

	public String getGroup() {
		return group;
	}

	public int getPack() {
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

	public double getPrice() {
		return price;
	}

	public int getStock() {
		return stock;
	}

	public String getOrigin() {
		return origin;
	}

	public boolean isDiscount() {
		return discount;
	}

	public int getTaxCode() {
		return taxCode;
	}

	public Date getLastModified() {
		return lastModified;
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

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public void setPack(int pack) {
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

	public void setPrice(double price) {
		this.price = price;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public void setDiscount(boolean discount) {
		this.discount = discount;
	}

	public void setTaxCode(int taxCode) {
		this.taxCode = taxCode;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	
	
	
}