package com.bexos.backend;

import com.bexos.backend.entitites.Product;
import com.bexos.backend.entitites.Role;
import com.bexos.backend.entitites.User;
import com.bexos.backend.repositories.ProductRepository;
import com.bexos.backend.repositories.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository, ProductRepository productRepository){

		return args -> {
			// Create Users
			createUser(userRepository);

			// Create Products
			createProduct(productRepository);
		};
	}

	void createUser(UserRepository userRepository) {
		if(userRepository.count() == 0){
			User admin1 = User.builder()
					.role(Role.ADMIN)
					.username("admin1")
					.password("password")
					.email("admin1@gmail.com")
					.build();
			userRepository.save(admin1);

			User admin2 = User.builder()
					.role(Role.ADMIN)
					.username("admin2")
					.password("password")
					.email("admin@gmail.com")
					.build();
			userRepository.save(admin2);

			for(int i = 1; i<10; i++){
				Faker faker = new Faker();
				String firstName = faker.name().firstName();
				String lastName = faker.name().lastName();
				String email = (firstName + "." + lastName + "@gmail.com");
				var user = User.builder()
						.email(email)
						.username(firstName + " " + lastName)
						.password("password")
						.role(Role.USER)
						.build();
				userRepository.save(user);
			}
		}
	}

	void createProduct(ProductRepository productRepository) {
		if(productRepository.count() == 0){
			for(int i = 1; i<10; i++){
				Faker faker = new Faker();
				String productName = faker.food().dish();
				var product = Product.builder()
						.productName(productName)
						.build();
				productRepository.save(product);
			}
		}
	}
}
