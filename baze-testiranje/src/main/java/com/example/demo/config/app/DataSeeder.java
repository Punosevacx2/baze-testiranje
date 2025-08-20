package com.example.demo.config.app;


import com.example.demo.mongo.entities.Role;
import com.example.demo.mongo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

	@Bean
	CommandLineRunner seedRoles(RoleRepository roleRepository) {
		return args -> {
			roleRepository.findByName("USER")
					.orElseGet(() -> roleRepository.save(new Role(null, "USER")));
			roleRepository.findByName("ADMIN")
					.orElseGet(() -> roleRepository.save(new Role(null, "ADMIN")));
		};
	}
}
