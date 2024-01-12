package PriceCategory.PriceService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PriceCategory.PriceService.model.ERPCustomer;
import PriceCategory.PriceService.service.ERPCustomerService;
import PriceCategory.PriceService.service.SecurityService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/erpcustomer")
public class ERPCustomerController {

	@Autowired
	private ERPCustomerService customerService;

	@Autowired
	private SecurityService securityService;

	public ERPCustomerController(ERPCustomerService customerService, SecurityService securityService) {
		this.customerService = customerService;
		this.securityService = securityService;
	}

	@PostMapping("/add/list")
	public ResponseEntity<List<ERPCustomer>> addErpCustomer(HttpServletRequest request,@RequestBody List<ERPCustomer> list) {
		String preSharedKey = request.getHeader("Authorization");
		if (!securityService.validateKey(preSharedKey))
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		
		List<ERPCustomer> addedCustomer = customerService.addListOfERPCustomer(list);
		return new ResponseEntity<List<ERPCustomer>>(addedCustomer, HttpStatus.OK);
	}

	@DeleteMapping("/delete/all")
	public ResponseEntity<Boolean> deleteAllErpCustomer(HttpServletRequest request) {
		String preSharedKey = request.getHeader("Authorization");
		if (!securityService.validateKey(preSharedKey))
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		
		boolean success = customerService.deleteAll();
		if (success)
			return new ResponseEntity<Boolean>(success,HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(success,HttpStatus.BAD_REQUEST);
	}
}
