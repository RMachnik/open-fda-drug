package com.rmachnik.drugs.api;

import com.rmachnik.drugs.application.DrugApplicationService;
import com.rmachnik.drugs.domain.DrugApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DrugApplicationControllerTest {

    @Mock
    private DrugApplicationService service;

    @InjectMocks
    private DrugApplicationController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    @Test
    void testSearchDrugs() throws Exception {
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
}