package apiserver.apiserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import apiserver.apiserver.model.User;

public interface UserRepo extends JpaRepository<User, Long>{
	
	

}
