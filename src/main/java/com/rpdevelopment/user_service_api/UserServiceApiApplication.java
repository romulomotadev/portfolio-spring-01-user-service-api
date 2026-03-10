package com.rpdevelopment.user_service_api;

import com.rpdevelopment.user_service_api.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApiApplication {

	@Autowired
	private SecurityConfig securityConfig;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApiApplication.class, args);
	}

}
