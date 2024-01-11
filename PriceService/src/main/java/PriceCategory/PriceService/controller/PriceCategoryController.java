package PriceCategory.PriceService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PriceCategory.PriceService.model.PriceCategory;
import PriceCategory.PriceService.model.Product;
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

	public PriceCategoryController(PriceCategoryService priceService, SecurityService securityService) {
		this.priceService = priceService;
		this.securityService = securityService;
	}

	@PostMapping("/add/list")
	public ResponseEntity<List<PriceCategory>> addPriceCategory(HttpServletRequest request, List<PriceCategory> list) {
		String preSharedKey = request.getHeader("Authorization");
		if (!securityService.validateKey(preSharedKey))
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);

		List<PriceCategory> addedList = priceService.addListOfPriceCategory(list);
		return new ResponseEntity<List<PriceCategory>>(list, HttpStatus.OK);
	}

	@DeleteMapping("/delete/all")
	public ResponseEntity<Boolean> deleteAllPriceMapping(HttpServletRequest request) {
		String preSharedKey = request.getHeader("Authorization");
		if (!securityService.validateKey(preSharedKey))
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);

		boolean success = priceService.deleteAll();
		if (success) return new ResponseEntity<Boolean>(success, HttpStatus.OK);
		else return new ResponseEntity<Boolean>(success, HttpStatus.BAD_REQUEST);
	}

}
