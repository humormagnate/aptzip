package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
// @EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
@EnableAutoConfiguration()
@SpringBootApplication
public class AptzipApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AptzipApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AptzipApplication.class);
	}
	
}
