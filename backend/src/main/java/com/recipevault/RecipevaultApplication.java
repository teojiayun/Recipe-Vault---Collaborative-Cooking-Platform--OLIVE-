package com.recipevault;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.recipevault")
public class RecipevaultApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipevaultApplication.class, args);
	}

}
