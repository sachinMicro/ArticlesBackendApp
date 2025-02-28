package com.articles;


import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.articles.model.User;
import com.articles.repository.UserRepository;

@SpringBootApplication
public class ArticlesBackendAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArticlesBackendAppApplication.class, args);
	}

	   @Bean
	   CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	        return args -> {
	            if (userRepository.findByUsername("admin").isEmpty()) {
	                User admin = new User();
	                admin.setUsername("admin");
	                admin.setPassword(passwordEncoder.encode("admin"));
	                admin.setRoles(Set.of("ADMIN", "USER"));
	                userRepository.save(admin);
	            }
	            if (userRepository.findByUsername("user").isEmpty()) {
	                User user = new User();
	                user.setUsername("user");
	                user.setPassword(passwordEncoder.encode("user"));
	                user.setRoles(Set.of("USER"));
	                userRepository.save(user);
	            }
	        };
	    }
	}
