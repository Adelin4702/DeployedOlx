package com.olxapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class serves as the main entry point for the OLX application.
 * It bootstraps the Spring Boot application context and enables its features.
 */
@SpringBootApplication
public class OlxApplication {
	/**
	 * The main method for launching the OLX application.
	 *
	 * @param args Command-line arguments (not used in this application).
	 */
	public static void main(String[] args) {
		SpringApplication.run(OlxApplication.class, args);
	}
}
