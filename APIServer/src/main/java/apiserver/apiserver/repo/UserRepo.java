package apiserver.apiserver.repo;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import apiserver.apiserver.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Set<User> findByUsernameContainingIgnoreCase(String keyword);

	boolean existsByUsername(String username);
	
	Optional<User> findByErpId(Long erpId);

	@Query("SELECT u.username FROM User u")
	Set<String> findAllUsernames();

	@Query("SELECT u.erpId FROM User u WHERE u.erpId IS NOT NULL")
	Set<String> findAllErpId();

}
