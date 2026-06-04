package com.omarrdev.ithra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class IthraApplication {

	public static void main(String[] args) {
		SpringApplication.run(IthraApplication.class, args);
	}

}
