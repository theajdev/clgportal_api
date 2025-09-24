package com.aj.clgportal;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClgportalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClgportalApplication.class, args);
	}
	
	@Bean
	ModelMapper ModelMapper() {
		return new ModelMapper();
	}

}
