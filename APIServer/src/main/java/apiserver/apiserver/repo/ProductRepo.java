package apiserver.apiserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import apiserver.apiserver.dto.CategoryListDTO;
import apiserver.apiserver.model.Product;
import jakarta.transaction.Transactional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
	
	@Modifying
	@Transactional
	@Query("UPDATE Product p SET p.deleted = :deleted WHERE p.productId = :productId")
	Product updateDeletedStatus(Long productId, boolean deleted);
	
	   // Retrieve a list of distinct brands
    @Query("SELECT DISTINCT p.brand FROM Product p")
    List<String> findDistinctBrands();

    // Retrieve a list of distinct categories
    @Query("SELECT DISTINCT p.category FROM Product p")
    List<String> findDistinctCategories();

    // Retrieve a list of distinct product groups
    @Query("SELECT DISTINCT p.subCategory FROM Product p")
    List<String> findDistinctSubCategory();

    // Retrieve a list of distinct origins
    @Query("SELECT DISTINCT p.origin FROM Product p")
    List<String> findDistinctOrigins();
    
    @Query(value = "SELECT p.category AS category, " +
            "GROUP_CONCAT(p.subCategory) AS subCategory, " +
            "GROUP_CONCAT(p.brand) AS brands, " +
            "GROUP_CONCAT(p.origin) AS origins " +
            "FROM Product p " +
            "GROUP BY p.category")
     List<CategoryListDTO> getCategoryList();
	
}
