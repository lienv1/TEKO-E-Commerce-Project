package PriceCategory.PriceService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ERPCustomer {

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Integer subcategory;
	private Integer category;

	public ERPCustomer() {

	}

	public Long getId() {
		return id;
	}

	public Integer getSubcategory() {
		return subcategory;
	}

	public Integer getCategory() {
		return category;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSubcategory(Integer subcategory) {
		this.subcategory = subcategory;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

}
