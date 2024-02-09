package PriceCategory.PriceService.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PriceCategory.PriceService.exception.ERPCustomerNotFoundException;
import PriceCategory.PriceService.model.ERPCustomer;
import PriceCategory.PriceService.model.PriceCategory;
import PriceCategory.PriceService.model.PriceCategoryDTO;
import PriceCategory.PriceService.model.Product;
import PriceCategory.PriceService.model.ProductDTO;
import PriceCategory.PriceService.service.ERPCustomerService;
import PriceCategory.PriceService.service.PriceCategoryService;
import PriceCategory.PriceService.service.SecurityService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/pricecategory")
public class PriceCategoryController {

	@Autowired
	private PriceCategoryService priceService;

	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private ERPCustomerService erpCustomerService;

	public PriceCategoryController(PriceCategoryService priceService, SecurityService securityService, ERPCustomerService erpCustomerService) {
		this.priceService = priceService;
		this.securityService = securityService;
		this.erpCustomerService = erpCustomerService;
	}

	@PostMapping("/add/list")
	public ResponseEntity<List<PriceCategory>> addPriceCategory(HttpServletRequest request,
			@RequestBody List<PriceCategory> list) {
		String preSharedKey = request.getHeader("Authorization");
		if (!securityService.validateKey(preSharedKey))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		if (list == null || list.isEmpty()) {
			System.out.println("List is empty");
			return ResponseEntity.badRequest().build();
		}

		System.out.println(list.size());

		List<PriceCategory> addedList = priceService.addListOfPriceCategory(list);
		return new ResponseEntity<List<PriceCategory>>(addedList, HttpStatus.OK);
	}

	@DeleteMapping("/delete/all")
	public ResponseEntity<Boolean> deleteAllPriceMapping(HttpServletRequest request) {
		String preSharedKey = request.getHeader("Authorization");
		if (!securityService.validateKey(preSharedKey))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		boolean success = priceService.deleteAll();
		if (success)
			return new ResponseEntity<Boolean>(success, HttpStatus.OK);
		else
			return new ResponseEntity<Boolean>(success, HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/quantity/{quantity}/customer/{erpid}")
	public ResponseEntity<Double> getPrice(HttpServletRequest request,@PathVariable("quantity") int quantity, @PathVariable("erpid") Long erpId, @RequestBody(required = true) Product product)  {
		String preSharedKey = request.getHeader("Authorization");
		if (!securityService.validateKey(preSharedKey))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		Double price = 0.0;
		ERPCustomer erpcustomer;
		
		try {
			erpcustomer = erpCustomerService.getERPCustomer(erpId);
		} catch (ERPCustomerNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
		
		price = priceService.getPrice(product, erpcustomer,quantity);
		
		return new ResponseEntity<Double>(price,HttpStatus.OK);
	}
	
	@PostMapping("/customer/{erpid}")
	public ResponseEntity<List<PriceCategoryDTO>> getPrices(HttpServletRequest request, @PathVariable("erpid") Long erpId, @RequestBody(required = true) List<ProductDTO> products)  {
		String preSharedKey = request.getHeader("Authorization");
		if (!securityService.validateKey(preSharedKey))
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		
		ERPCustomer erpcustomer;
		
		try {
			erpcustomer = erpCustomerService.getERPCustomer(erpId);
		} catch (ERPCustomerNotFoundException e) {
			System.out.println("Customer doesn't exist");
			return ResponseEntity.notFound().build();
		}
		
		ArrayList<PriceCategoryDTO> list = new ArrayList<PriceCategoryDTO>();
		
		for (ProductDTO product : products) {
			PriceCategoryDTO priceCategoryDTO = new PriceCategoryDTO();
			double price = priceService.getPrice(product.getProduct(), erpcustomer,product.getQuantity());
			priceCategoryDTO.setPrice(price);
			priceCategoryDTO.setProductId(product.getProduct().getProductId());
			list.add(priceCategoryDTO);
		}
		
		return new ResponseEntity<List<PriceCategoryDTO>>(list,HttpStatus.OK);
	}

}
