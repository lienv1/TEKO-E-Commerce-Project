package PriceCategory.PriceService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import PriceCategory.PriceService.model.PriceCategory;

@Repository
public interface PriceCategoryRepo extends JpaRepository<PriceCategory, Long> {

	@Query("SELECT pc FROM PriceCategory pc "
			+ "WHERE (pc.product = :productId OR pc.product = :productCategory OR pc.product = :productSubCategory OR pc.product IS NULL) "
			+ "AND (pc.customer = :customerId OR pc.customer = :customerGroup OR pc.customer = :customerCategory OR pc.customer = 0)")
	List<PriceCategory> findPriceCategories(String productId, String productCategory, String productSubCategory,
			Long customerId, Integer customerGroup, Integer customerCategory);

}
