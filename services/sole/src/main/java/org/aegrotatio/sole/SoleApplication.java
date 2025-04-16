package org.aegrotatio.sole;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SoleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SoleApplication.class, args);
	}

}
