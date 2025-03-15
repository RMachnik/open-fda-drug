package com.rmachnik.drugs.application;

import com.rmachnik.drugs.domain.DrugApplication;
import com.rmachnik.drugs.domain.repository.DrugApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DrugApplicationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DrugApplicationRepository repository;

    @InjectMocks
    private DrugApplicationService service;

    @Test
    void testSearchDrugs() {
        // Arrange
        String manufacturer = "Renew Pharmaceuticals";
        String brand = "INDOCYANINE GREEN";
        int limit = 10;
        int skip = 0;
        String url = "https://api.fda.gov/drug/drugsfda.json?search=manufacturer_name:" + manufacturer + "+brand_name:" + brand + "&limit=" + limit + "&skip=" + skip;

        Map<String, Object> apiResponse = Map.of(
                "results", List.of(
                        Map.of(
                                "application_number", "ANDA040811",
                                "openfda", Map.of(
                                        "manufacturer_name", List.of("Renew Pharmaceuticals"),
                                        "product_ndc", List.of("73624-424", "70100-424"),
                                        "generic_name", List.of("INDOCYANINE GREEN")
                                )
                        )
                )
        );

        when(restTemplate.getForObject(url, Map.class)).thenReturn(apiResponse);

        // Act
        List<DrugApplication> drugApplications = service.searchDrugs(manufacturer, brand, limit, skip);

        // Assert
        assertNotNull(drugApplications);
        assertEquals(1, drugApplications.size());
        DrugApplication drug = drugApplications.get(0);
        assertEquals("ANDA040811", drug.getApplicationNumber());
        assertEquals("Renew Pharmaceuticals", drug.getManufacturerName());
        assertEquals("INDOCYANINE GREEN", drug.getSubstanceName());
        assertEquals(2, drug.getProductNumbers().size());

        // Verify that the repository save method was called
        verify(repository, times(1)).save(any(DrugApplication.class));
    }

    @Test
    void testGetAllStoredApplications() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<DrugApplication> drugApplications = List.of(
                new DrugApplication("ANDA040811", "Renew Pharmaceuticals", "INDOCYANINE GREEN", List.of("73624-424", "70100-424"))
        );
        Page<DrugApplication> page = new PageImpl<>(drugApplications);

        when(repository.findAll(pageable)).thenReturn(page);

        // Act
        Page<DrugApplication> result = service.getAllStoredApplications(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        DrugApplication drug = result.getContent().get(0);
        assertEquals("ANDA040811", drug.getApplicationNumber());
        assertEquals("Renew Pharmaceuticals", drug.getManufacturerName());
        assertEquals("INDOCYANINE GREEN", drug.getSubstanceName());
        assertEquals(2, drug.getProductNumbers().size());
    }
}