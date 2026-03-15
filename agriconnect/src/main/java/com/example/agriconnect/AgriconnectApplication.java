package com.example.agriconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.agriconnect.repository")
@EntityScan(basePackages = "com.example.agriconnect.classes")
public class AgriconnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgriconnectApplication.class, args);
	}

}
