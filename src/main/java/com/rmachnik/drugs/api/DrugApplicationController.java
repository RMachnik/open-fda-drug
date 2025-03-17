package com.rmachnik.drugs.api;


import com.rmachnik.drugs.api.dto.DrugApplicationDTO;
import com.rmachnik.drugs.application.DrugApplicationService;
import com.rmachnik.drugs.domain.DrugApplication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Drug Applications", description = "APIs for searching and storing FDA drug applications. All endpoints may return a 404 if no data is found or if the endpoint is invalid.")
@RestController
@Validated
@RequestMapping("/drugs/applications")
public class DrugApplicationController {
    private final DrugApplicationService service;

    public DrugApplicationController(DrugApplicationService service) {
        this.service = service;
    }

    private static DrugApplication toDrugApplication(DrugApplicationDTO dto) {
        return new DrugApplication(
                dto.applicationNumber(),
                dto.manufacturerName(),
                dto.substanceName(),
                dto.productNumbers()
        );
    }

    private static DrugApplicationDTO toDto(DrugApplication savedDrug) {
        return new DrugApplicationDTO(
                savedDrug.getApplicationNumber(),
                savedDrug.getManufacturerName(),
                savedDrug.getSubstanceName(),
                savedDrug.getProductNumbers()
        );
    }

    @Operation(summary = "Search FDA drug applications by manufacturer",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful search"),
                    @ApiResponse(responseCode = "404", description = "No applications found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping("/search")
    public List<DrugApplicationDTO> searchDrugs(
            @RequestParam @NotBlank(message = "Manufacturer is required") String manufacturer,
            @RequestParam(required = false) String brand,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "0") int page) {

        List<DrugApplicationDTO> results = service.searchDrugs(manufacturer, brand, pageSize, pageSize * page).stream()
                .map(DrugApplicationController::toDto)
                .toList();
        if (results.isEmpty()) {
            throw new ResourceNotFoundException("No drug applications found for manufacturer: " + manufacturer);
        }
        return results;
    }

    @Operation(summary = "Save a drug application", description = "Store a drug application in the database")
    @PostMapping("/")
    public ResponseEntity<DrugApplicationDTO> saveDrug(@Valid @RequestBody DrugApplicationDTO dto) {
        DrugApplication drugApplication = toDrugApplication(dto);

        DrugApplication savedDrug = service.saveDrugApplication(drugApplication);

        DrugApplicationDTO responseDTO = toDto(savedDrug);

        return ResponseEntity.ok(responseDTO);
    }

    @Operation(summary = "Get stored drug applications", description = "Retrieve paginated stored drug applications")
    @GetMapping("/")
    public Page<DrugApplicationDTO> getStoredDrugs(Pageable pageable) {
        return service.getAllStoredApplications(pageable).map(DrugApplicationController::toDto);
    }
}
