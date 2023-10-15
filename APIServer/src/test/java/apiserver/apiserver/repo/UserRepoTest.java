package apiserver.apiserver.repo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import apiserver.apiserver.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepoTest {

	@Autowired
	private UserRepo userRepo;
	
	private User user;

	
	@BeforeEach
	void initUser() {
		user = new User();
		user.setFirstname("John");
		user.setLastname("Doe");
		user.setEmail("john.doe@example.com");
		user.setUsername("johndoe");
	}
	
	@Test
	void saveTest() {
		User savedUser = userRepo.save(user);
		assertNotNull(savedUser.getUserId()); // Assuming your User entity has an ID field
		assertEquals("John", savedUser.getFirstname());
		assertEquals("Doe", savedUser.getLastname());
		assertEquals("john.doe@example.com", savedUser.getEmail());
		assertEquals("johndoe", savedUser.getUsername());
	}
	
	@Test
	void saveTestFail() {	
		userRepo.saveAndFlush(user);
		User user2 = new User();
		user2.setUsername("johndoe");
		assertThrows(DataIntegrityViolationException.class, () -> {
	        userRepo.saveAndFlush(user2);  // use saveAndFlush to force JPA to write to the database immediately
	    });
	}

}
