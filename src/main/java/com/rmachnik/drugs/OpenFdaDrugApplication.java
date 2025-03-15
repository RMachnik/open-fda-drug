package com.rmachnik.drugs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@OpenAPIDefinition(
		info = @Info(title = "OpenFDA Drug API", version = "1.0", description = "API for searching and storing drug applications")
)
@SpringBootApplication
public class OpenFdaDrugApplication {
	public static void main(String[] args) {
		SpringApplication.run(OpenFdaDrugApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
