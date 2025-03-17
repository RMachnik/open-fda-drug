package com.rmachnik.drugs.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmachnik.drugs.domain.DrugApplication;
import com.rmachnik.drugs.domain.repository.DrugApplicationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DrugApplicationService {
    private static final Logger log = LoggerFactory.getLogger(DrugApplicationService.class);

    private final RestTemplate restTemplate;
    private final DrugApplicationRepository repository;
    private final String fdaUrl;

    public DrugApplicationService(RestTemplate restTemplate, DrugApplicationRepository repository, @Value("${fda.url}") String fdaUrl) {
        this.restTemplate = restTemplate;
        this.repository = repository;
        this.fdaUrl = fdaUrl;
    }

    public List<DrugApplication> searchDrugs(String manufacturer, String brand, int limit, int skip) {
        log.info("Searching drugs for manufacturer: {}, brand: {}, limit: {}, skip: {}", manufacturer, brand, limit, skip);
        UriComponentsBuilder url = UriComponentsBuilder.fromUriString(fdaUrl)
                .path("/drug/drugsfda.json");
        String searchParams = "manufacturer_name:" + manufacturer;
        if (brand != null && !brand.isEmpty()) {
            searchParams += "+brand_name:" + brand;
        }
        url.queryParam("search", searchParams)
                .queryParam("limit", limit)
                .queryParam("skip", skip);
        try {
            URI uri = url.build().encode().toUri();
            log.info("Calling FDA API with URL: {}", uri);
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
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
        String manufacturerName = manufacturerNames != null && !manufacturerNames.isEmpty() ? manufacturerNames.getFirst() : "Unknown";
        String substanceName = ((List<String>) ((Map<String, Object>) result.get("openfda")).get("generic_name")).getFirst();

        return new DrugApplication(applicationNumber, manufacturerName, substanceName, productNumbers);
    }

    public DrugApplication saveDrugApplication(DrugApplication drugApplication) {
        return repository.save(drugApplication);
    }

    public Page<DrugApplication> getAllStoredApplications(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
