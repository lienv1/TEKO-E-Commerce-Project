package PriceCategory.PriceService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PriceCategory.PriceService.model.ERPCustomer;

@Repository
public interface ERPCustomerRepo extends JpaRepository<ERPCustomer, Long>{

	
	
}
