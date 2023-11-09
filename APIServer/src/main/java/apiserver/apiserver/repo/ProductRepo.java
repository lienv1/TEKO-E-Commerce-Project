package apiserver.apiserver.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import apiserver.apiserver.dto.CategoryListDTO;
import apiserver.apiserver.model.Product;
import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
	
	@Modifying
	@Transactional
	@Query("UPDATE Product p SET p.deleted = :deleted WHERE p.productId = :productId")
	Product updateDeletedStatus(Long productId, boolean deleted);

    @Query(value = "SELECT p.category AS category, " +
            "GROUP_CONCAT(p.subCategory) AS subCategory " +
            "FROM Product p " +
            "GROUP BY p.category")
     Set<Tuple> getCategoryList();
    
    @Query("SELECT p.category AS category, p.subCategory AS subCategory FROM Product p")
    List<String[]> findCategoriesAndSubcategories();
	
}
