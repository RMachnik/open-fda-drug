package com.rmachnik.drugs.api;

import com.rmachnik.drugs.application.DrugApplicationService;
import com.rmachnik.drugs.domain.DrugApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DrugApplicationControllerTest {

    @Mock
    private DrugApplicationService service;

    @InjectMocks
    private DrugApplicationController controller;

    private MockMvc mockMvc;

    @Test
    void testSearchDrugs() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        String manufacturer = "Renew Pharmaceuticals";
        String brand = "INDOCYANINE GREEN";
        int limit = 10;
        int skip = 0;

        List<DrugApplication> drugApplications = List.of(
                new DrugApplication("ANDA040811", "Renew Pharmaceuticals", "INDOCYANINE GREEN", List.of("73624-424", "70100-424"))
        );

        when(service.searchDrugs(manufacturer, brand, limit, skip)).thenReturn(drugApplications);

        mockMvc.perform(get("/drugs/search")
                        .param("manufacturer", manufacturer)
                        .param("brand", brand)
                        .param("limit", String.valueOf(limit))
                        .param("skip", String.valueOf(skip)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].applicationNumber").value("ANDA040811"))
                .andExpect(jsonPath("$[0].manufacturerName").value("Renew Pharmaceuticals"))
                .andExpect(jsonPath("$[0].substanceName").value("INDOCYANINE GREEN"))
                .andExpect(jsonPath("$[0].productNumbers[0]").value("73624-424"))
                .andExpect(jsonPath("$[0].productNumbers[1]").value("70100-424"));
    }

    @Test
    void testSaveDrug() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        DrugApplication drugApplication = new DrugApplication("ANDA040811", "Renew Pharmaceuticals", "INDOCYANINE GREEN", List.of("73624-424", "70100-424"));

        when(service.saveDrugApplication(any(DrugApplication.class))).thenReturn(drugApplication);

        mockMvc.perform(post("/drugs/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"applicationNumber\":\"ANDA040811\",\"manufacturerName\":\"Renew Pharmaceuticals\",\"substanceName\":\"INDOCYANINE GREEN\",\"productNumbers\":[\"73624-424\",\"70100-424\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationNumber").value("ANDA040811"))
                .andExpect(jsonPath("$.manufacturerName").value("Renew Pharmaceuticals"))
                .andExpect(jsonPath("$.substanceName").value("INDOCYANINE GREEN"))
                .andExpect(jsonPath("$.productNumbers[0]").value("73624-424"))
                .andExpect(jsonPath("$.productNumbers[1]").value("70100-424"));
    }
    //todo fixme
    @Test
    void testGetStoredDrugs() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        List<DrugApplication> drugApplications = List.of(
                new DrugApplication("ANDA040811", "Renew Pharmaceuticals", "INDOCYANINE GREEN", List.of("73624-424", "70100-424"))
        );
        Page<DrugApplication> page = new PageImpl<>(drugApplications);

        when(service.getAllStoredApplications(pageable)).thenReturn(page);

        mockMvc.perform(get("/drugs/stored")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].applicationNumber").value("ANDA040811"))
                .andExpect(jsonPath("$.content[0].manufacturerName").value("Renew Pharmaceuticals"))
                .andExpect(jsonPath("$.content[0].substanceName").value("INDOCYANINE GREEN"))
                .andExpect(jsonPath("$.content[0].productNumbers[0]").value("73624-424"))
                .andExpect(jsonPath("$.content[0].productNumbers[1]").value("70100-424"));
    }
}