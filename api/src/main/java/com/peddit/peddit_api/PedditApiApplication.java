package com.peddit.peddit_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class PedditApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PedditApiApplication.class, args);
	}

}
