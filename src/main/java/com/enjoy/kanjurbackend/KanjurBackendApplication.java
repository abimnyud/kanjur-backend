package com.enjoy.kanjurbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class KanjurBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(KanjurBackendApplication.class, args);
	}

}
