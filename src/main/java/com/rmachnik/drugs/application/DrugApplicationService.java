package com.rmachnik.drugs.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmachnik.drugs.domain.DrugApplication;
import com.rmachnik.drugs.domain.repository.DrugApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DrugApplicationService {
    private static final Logger log = LoggerFactory.getLogger(DrugApplicationService.class);

    private final RestTemplate restTemplate;
    private final DrugApplicationRepository repository;

    public DrugApplicationService(RestTemplate restTemplate, DrugApplicationRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    public List<DrugApplication> searchDrugs(String manufacturer, String brand, int limit, int skip) {
        String url = "https://api.fda.gov/drug/drugsfda.json?search=manufacturer_name:" + manufacturer;
        if (brand != null && !brand.isEmpty()) {
            url += "+brand_name:" + brand;
        }
        url += "&limit=" + limit + "&skip=" + skip;
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode json = objectMapper.readTree(response.getBody());
            JsonNode resultsJson = json.get("results");
            List<Map<String, Object>> results = objectMapper.convertValue(resultsJson, new TypeReference<>() {
            });
            return results.stream().map(this::mapToDrugApplication).collect(Collectors.toList());
        } catch (HttpClientErrorException | JsonProcessingException e) {
            log.info("Unable to retrieve drug applications from FDA API: {}", e.getMessage(), e);
            return List.of();
        }
    }

    private DrugApplication mapToDrugApplication(Map<String, Object> result) {
        String applicationNumber = (String) result.get("application_number");
        List<String> manufacturerNames = (List<String>) ((Map<String, Object>) result.get("openfda")).get("manufacturer_name");
        List<String> productNumbers = (List<String>) ((Map<String, Object>) result.get("openfda")).get("product_ndc");
        String manufacturerName = manufacturerNames != null && !manufacturerNames.isEmpty() ? manufacturerNames.get(0) : "Unknown";
        String substanceName = ((List<String>) ((Map<String, Object>) result.get("openfda")).get("generic_name")).get(0);

        return new DrugApplication(applicationNumber, manufacturerName, substanceName, productNumbers);
    }

    public DrugApplication saveDrugApplication(DrugApplication drugApplication) {
        return repository.save(drugApplication);
    }

    public Page<DrugApplication> getAllStoredApplications(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
