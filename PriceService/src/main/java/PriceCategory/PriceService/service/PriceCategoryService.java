package PriceCategory.PriceService.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import PriceCategory.PriceService.model.ERPCustomer;
import PriceCategory.PriceService.model.PriceCategory;
import PriceCategory.PriceService.model.Product;
import PriceCategory.PriceService.repository.PriceCategoryRepo;

@Service
public class PriceCategoryService {

	@Autowired
	private PriceCategoryRepo priceRepo;

	public PriceCategoryService(PriceCategoryRepo priceRepo) {
		this.priceRepo = priceRepo;
	}

	public Double getPrice(Product product, ERPCustomer customer, double quantity) {

		Double price = product.getPrice();

		String productid = product.getProductId().toString();
		String category = product.getCategory();
		String subcategory = product.getSubCategory();
		String collection = product.getCollection();

		Long erpId = customer.getId();
		Integer customerGroup = customer.getSubcategory();
		Integer customerCategory = customer.getCategory();
		
		List<PriceCategory> optionalPCs = priceRepo.findPriceCategories(productid, 
				category, 
				subcategory, 
				erpId, 
				customerGroup, 
				customerCategory,
				quantity,
				collection
				);
		PriceCategory priceCategory = optionalPCs.get(0);

		if (priceCategory != null) {
			boolean hasPercent = priceCategory.getPercent();
			if (hasPercent)
				price = price - (price / 100 * priceCategory.getValue());
			else price = priceCategory.getValue();
		}
			
		return price;
	}

	public List<PriceCategory> addListOfPriceCategory(List<PriceCategory> pcs) {
		return priceRepo.saveAll(pcs);
	}

	public boolean deleteAll() {
		try {
			priceRepo.deleteAll();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
