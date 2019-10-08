package com.trilogyed.levelup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LevelupApplication {

	public static void main(String[] args) {
		SpringApplication.run(LevelupApplication.class, args);
	}

}
