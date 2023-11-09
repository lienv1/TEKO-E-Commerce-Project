package apiserver.apiserver.dto;

import java.util.Set;

public class FilterDTO {
	
	Set<String> origins;
	
	Set<String> brands;
	
	public FilterDTO() {
		
	}

	public Set<String> getOrigins() {
		return origins;
	}

	public Set<String> getBrands() {
		return brands;
	}

	public void setOrigins(Set<String> origins) {
		this.origins = origins;
	}

	public void setBrands(Set<String> brands) {
		this.brands = brands;
	}
	
	

}
