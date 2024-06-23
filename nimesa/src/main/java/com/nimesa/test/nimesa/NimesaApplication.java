package com.nimesa.test.nimesa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.nimesa.test.nimesa.repository")
public class NimesaApplication {

	public static void main(String[] args) {
		SpringApplication.run(NimesaApplication.class, args);
	}

}
