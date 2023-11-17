package com.casa.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

//TODO remove security disabled
@SpringBootApplication(
		exclude = { SecurityAutoConfiguration.class })
public class CasaBackApplication {

	public static void main(String[] args) {
			SpringApplication.run(CasaBackApplication.class, args);
	}

}
