package apiserver.apiserver.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import apiserver.apiserver.dto.FilterDTO;
import apiserver.apiserver.model.Product;
import jakarta.transaction.Transactional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
	
	@Query("SELECT p FROM Product p WHERE p.deleted = false")
    Page<Product> findAllActiveProducts(Pageable pageable);
	
	@Modifying
	@Transactional
	@Query("UPDATE Product p SET p.deleted = :deleted WHERE p.productId = :productId")
	void updateDeletedStatus(Long productId, boolean deleted);
	
	@Modifying
	@Transactional
	@Query("UPDATE Product p SET p.deleted = :deleted")
	void updateAllDeletedStatus(boolean deleted);
    
    @Query("SELECT p.category AS category, p.subCategory AS subCategory FROM Product p WHERE p.deleted = false")
    List<String[]> findCategoriesAndSubcategories();	
    
       
    //CATEGORY
    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.deleted = false AND (:category IS NULL OR p.category IN :category) AND (:subCategory IS NULL OR p.subCategory IN :subCategory) ORDER BY p.brand")
    Set<String> findDistinctBrands(@Nullable List<String> category, @Nullable List<String> subCategory);
    @Query("SELECT DISTINCT p.origin FROM Product p WHERE p.deleted = false AND (:category IS NULL OR p.category IN :category) AND (:subCategory IS NULL OR p.subCategory IN :subCategory) ORDER BY p.origin")
    Set<String> findDistinctOrigins(@Nullable List<String> category, @Nullable List<String> subCategory);
    
    
    //FAVORITE
    @Query("SELECT DISTINCT p FROM Product p WHERE p.deleted = false AND EXISTS (SELECT f FROM Favorite f WHERE f.product = p AND f.user.username = :username) AND (:brands IS NULL OR p.brand IN :brands) AND (:origins IS NULL OR p.origin IN :origins)")
    Page<Product> findFavoriteProductsByUserUsername(String username, Pageable pageable, @Nullable List<String> brands, @Nullable List<String> origins);
    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.deleted = false AND EXISTS (SELECT f FROM Favorite f WHERE f.product = p AND f.user.username = :username)")
    Set<String> findFavoriteBrands(String username);
    @Query("SELECT DISTINCT p.origin FROM Product p WHERE p.deleted = false AND EXISTS (SELECT f FROM Favorite f WHERE f.product = p AND f.user.username = :username)")
    Set<String> findFavoriteOrigins(String username);

    
}
