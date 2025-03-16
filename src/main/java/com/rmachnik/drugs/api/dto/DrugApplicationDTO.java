package com.rmachnik.drugs.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record DrugApplicationDTO(@NotBlank(message = "Application number is required") String applicationNumber,
                                 @NotBlank(message = "Manufacturer name is required") String manufacturerName,
                                 @NotBlank(message = "Substance name is required") String substanceName,
                                 @NotEmpty(message = "At least one product number is required") List<String> productNumbers) {
}
