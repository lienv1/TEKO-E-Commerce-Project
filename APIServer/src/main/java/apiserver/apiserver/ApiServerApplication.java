package apiserver.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiServerApplication {

	public static void main(String[] args) {
		System.out.println("Start");
		SpringApplication.run(ApiServerApplication.class, args);
	}

}
