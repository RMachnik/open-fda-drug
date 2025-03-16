package com.rmachnik.drugs;

import com.rmachnik.drugs.domain.DrugApplication;
import com.rmachnik.drugs.domain.repository.DrugApplicationRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@OpenAPIDefinition(
        info = @Info(title = "OpenFDA Drug API", version = "1.0", description = "API for searching and storing drug applications")
)
@SpringBootApplication
public class OpenFdaDrugApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenFdaDrugApplication.class, args);
    }


    @Bean
    public CommandLineRunner demo(DrugApplicationRepository repository) {
        return (args) -> {
            repository.save(new DrugApplication("ANDA040811", "Renew Pharmaceuticals", "INDOCYANINE GREEN", List.of("73624-424", "70100-424")));
            repository.save(new DrugApplication("BLA125244", "Hospira Inc.", "Foscarnet", List.of("17478-424")));
            repository.save(new DrugApplication("NDA021223", "Genentech Inc.", "RITUXAN", List.of("50242-405", "50242-406")));
            repository.save(new DrugApplication("NDA212725", "Genentech Inc.", "RITUXAN HYCELA", List.of("50242-407", "50242-408")));
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
