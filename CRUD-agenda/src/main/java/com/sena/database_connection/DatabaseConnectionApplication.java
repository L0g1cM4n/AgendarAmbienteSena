package com.sena.database_connection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.sena.database_connection")
public class DatabaseConnectionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseConnectionApplication.class, args);
	}

}