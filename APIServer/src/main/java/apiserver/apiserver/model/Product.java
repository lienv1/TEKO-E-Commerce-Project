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
	private Long productId; // Change the type to Long

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

	// Constructors, getters, setters, and other methods

	// Constructors
	public Product() {
		// Default constructor
	}

	public Product(String productName, String description, String brand, int weight, String category, String group,
			int pack, String searchIndex, String gtinUnit, String gtinPack, double price, int stock, String origin,
			boolean discount, int taxCode) {
		this.productName = productName;
		this.description = description;
		this.brand = brand;
		this.weight = weight;
		this.category = category;
		this.group = group;
		this.pack = pack;
		this.searchIndex = searchIndex;
		this.gtinUnit = gtinUnit;
		this.gtinPack = gtinPack;
		this.price = price;
		this.stock = stock;
	}
}