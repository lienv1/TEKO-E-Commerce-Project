package apiserver.apiserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apiserver.apiserver.dto.PriceCategoryDTO;
import apiserver.apiserver.dto.ProductDTO;
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
	
	@PostMapping("/quantity/{quantity}/erp/{erpId}")
	public ResponseEntity<Double> getPriceByErpId(@RequestBody Product product, @PathVariable("erpId") Long erpId, @PathVariable("quantity") int quantity){
		Double price = priceService.getPrice(erpId, product,quantity);
		if (price == null)
			return ResponseEntity.notFound().build();
		return new ResponseEntity<Double>(price,HttpStatus.OK);
	}
	
	@PostMapping("/erp/{erpId}")
	public ResponseEntity<List<PriceCategoryDTO>> getPricesByErpId(@RequestBody(required = true) List<ProductDTO> products, @PathVariable("erpId") Long erpId){
		List<PriceCategoryDTO> prices = priceService.getPrices(erpId, products);
		if (prices == null || prices.isEmpty())
			return ResponseEntity.notFound().build();
		return new ResponseEntity<List<PriceCategoryDTO>>(prices,HttpStatus.OK);
	}
	
}
