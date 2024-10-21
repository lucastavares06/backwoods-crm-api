package com.backwoodslabs.backwoods_crm_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BackwoodsCrmApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackwoodsCrmApiApplication.class, args);
	}

}
