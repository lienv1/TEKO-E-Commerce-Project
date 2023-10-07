package apiserver.apiserver.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {

	@Test
	public void testInitUser() throws Exception {
		User user = new User();
		user.setFirstname("John");
		user.setLastname("Doe");
		user.setEmail("john.doe@example.com");
		user.setUsername("johndoe");

		assertEquals(user.getUsername(), "johndoe");
		assertEquals(user.getFirstname(), "John");
		assertEquals(user.getLastname(), "Doe");
		assertEquals(user.getEmail(), "john.doe@example.com");
	}

}
