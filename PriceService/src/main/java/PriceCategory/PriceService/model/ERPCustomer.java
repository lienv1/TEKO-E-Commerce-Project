package PriceCategory.PriceService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ERPCustomer {

	@Id
	private Long id;
	private Integer group;
	private Integer category;
	
	
	public ERPCustomer() {
		
	}

	public Long getId() {
		return id;
	}


	public Integer getGroup() {
		return group;
	}


	public Integer getCategory() {
		return category;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public void setGroup(Integer group) {
		this.group = group;
	}


	public void setCategory(Integer category) {
		this.category = category;
	}

	
}
