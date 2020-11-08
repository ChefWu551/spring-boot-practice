package com.mountain.spring.boot.yaml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = {"classpath:beans.xml"})
@EnableAutoConfiguration
public class SpringBootYamlApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootYamlApplication.class, args);
	}

}
