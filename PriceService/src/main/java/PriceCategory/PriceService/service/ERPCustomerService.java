package PriceCategory.PriceService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import PriceCategory.PriceService.model.ERPCustomer;
import PriceCategory.PriceService.repository.ERPCustomerRepo;

@Service
public class ERPCustomerService {

	@Autowired
	private ERPCustomerRepo customerRepo;

	public ERPCustomerService(ERPCustomerRepo customerRepo) {
		this.customerRepo = customerRepo;
	}

	public ERPCustomer addERPCustomer(ERPCustomer customer) {
		return customerRepo.save(customer);
	}

	public List<ERPCustomer> addListOfERPCustomer(List<ERPCustomer> customers) {
		return customerRepo.saveAll(customers);
	}

	public boolean deleteAll() {
		try {
			customerRepo.deleteAll();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
