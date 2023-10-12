package apiserver.apiserver.repo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import apiserver.apiserver.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepoTest {

	@Autowired
	private UserRepo userRepo;

	@Test
	public void testSave() {
		User user = new User();
		user.setFirstname("John");
		user.setLastname("Doe");
		user.setEmail("john.doe@example.com");
		user.setUsername("johndoe2");

		User savedUser = userRepo.save(user);

		assertNotNull(savedUser.getId()); // Assuming your User entity has an ID field
		assertEquals("John", savedUser.getFirstname());
		assertEquals("Doe", savedUser.getLastname());
		assertEquals("john.doe@example.com", savedUser.getEmail());
		assertEquals("johndoe2", savedUser.getUsername());
	}

}
