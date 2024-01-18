package apiserver.apiserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apiserver.apiserver.model.Product;
import apiserver.apiserver.service.PriceService;

@RestController
@RequestMapping("/price")
public class PriceController {
	
	@Autowired
	private PriceService priceService;
	
	public PriceController(PriceService priceService) {
		this.priceService = priceService;
	}
	
	@PostMapping("/erp/{erpId}")
	public ResponseEntity<Double> getPriceByErpId(@RequestBody Product product, @PathVariable("erpId") Long erpId){
		Double price = priceService.getPrice(erpId, product);
		if (price == null)
			return ResponseEntity.notFound().build();
		return new ResponseEntity<Double>(price,HttpStatus.OK);
	}
	
}
