package com.rmachnik.drugs.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmachnik.drugs.api.dto.DrugApplicationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DrugApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        // Load test data
    void testGetStoredDrugs() throws Exception {
        mockMvc.perform(get("/drugs/applications/stored")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].applicationNumber").value("ANDA040813"));
    }

    @Test
    void testSearchDrugsWithEmptyParam() throws Exception {
        mockMvc.perform(get("/drugs/applications/search")
                        .param("manufacturer", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error['searchDrugs.manufacturer']").value("Manufacturer is required"));
    }

    @Test
    void testSearchDrugsNotFound() throws Exception {
        String manufacturer = "Unknown Manufacturer";

        mockMvc.perform(get("/drugs/applications/search")
                        .param("manufacturer", manufacturer)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.RESOURCE_NOT_FOUND").value("No drug applications found for manufacturer: Unknown Manufacturer"));
    }

    @Test
    public void testNotFound() throws Exception {
        mockMvc.perform(get("/example"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error.RESOURCE_NOT_FOUND").value("No endpoint GET /example."));
    }

    @Test
    void testSaveDrugApplication_ValidationFailure() throws Exception {
        DrugApplicationDTO invalidDto = new DrugApplicationDTO("", "", "", Collections.emptyList());

        mockMvc.perform(post("/drugs/applications/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error.productNumbers").value("At least one product number is required"))
                .andExpect(jsonPath("$.error.applicationNumber").value("Application number is required"))
                .andExpect(jsonPath("$.error.manufacturerName").value("Manufacturer name is required"))
                .andExpect(jsonPath("$.error.substanceName").value("Substance name is required"));
    }

    @Test
    void testSaveDrug() throws Exception {
        DrugApplicationDTO drugApplicationDto = new DrugApplicationDTO("ANDA040811", "Renew Pharmaceuticals", "INDOCYANINE GREEN", List.of("73624-424", "70100-424"));

        mockMvc.perform(post("/drugs/applications/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(drugApplicationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationNumber").value("ANDA040811"))
                .andExpect(jsonPath("$.manufacturerName").value("Renew Pharmaceuticals"))
                .andExpect(jsonPath("$.substanceName").value("INDOCYANINE GREEN"))
                .andExpect(jsonPath("$.productNumbers[0]").value("73624-424"))
                .andExpect(jsonPath("$.productNumbers[1]").value("70100-424"));
    }
}
