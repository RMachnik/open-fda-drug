package com.rmachnik.drugs.application;

import com.rmachnik.drugs.domain.DrugApplication;
import com.rmachnik.drugs.domain.repository.DrugApplicationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DrugApplicationService {
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
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");
            List<DrugApplication> drugApplications = results.stream().map(this::mapToDrugApplication).collect(Collectors.toList());
            drugApplications.forEach(this::saveDrugApplication);
            return drugApplications;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching data from Open FDA API: " + e.getMessage());
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
