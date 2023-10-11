package apiserver.apiserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import apiserver.apiserver.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>,JpaSpecificationExecutor<Product> {

}
