package com.rmachnik.drugs.api;


import com.rmachnik.drugs.application.DrugApplicationService;
import com.rmachnik.drugs.domain.DrugApplication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drugs")
@Tag(name = "Drug Application API", description = "Endpoints for searching and managing drug applications")
public class DrugApplicationController {
    private final DrugApplicationService service;

    public DrugApplicationController(DrugApplicationService service) {
        this.service = service;
    }

    @GetMapping("/search")
    @Operation(summary = "Search drug applications", description = "Search FDA drug applications by manufacturer and optional brand name")
    public List<DrugApplication> searchDrugs(@RequestParam String manufacturer,
                                             @RequestParam(required = false) String brand,
                                             @RequestParam(defaultValue = "10") int limit,
                                             @RequestParam(defaultValue = "0") int skip) {
        return service.searchDrugs(manufacturer, brand, limit, skip);
    }

    @PostMapping("/save")
    @Operation(summary = "Save a drug application", description = "Store a drug application in the database")
    public DrugApplication saveDrug(@RequestBody DrugApplication drugApplication) {
        return service.saveDrugApplication(drugApplication);
    }

    @GetMapping("/stored")
    @Operation(summary = "Get stored drug applications", description = "Retrieve paginated stored drug applications")
    public Page<DrugApplication> getStoredDrugs(Pageable pageable) {
        return service.getAllStoredApplications(pageable);
    }
}
