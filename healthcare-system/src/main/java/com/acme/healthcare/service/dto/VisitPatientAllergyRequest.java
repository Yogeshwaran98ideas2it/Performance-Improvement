package com.acme.healthcare.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO for creating or updating visit patient allergies.
 */
@Data
public class VisitPatientAllergyRequest {
    
    @NotNull(message = "Visit ID is required")
    private Long visitId;
    
    @NotBlank(message = "Allergen is required")
    private String allergen;
    
    private String reaction;
    
    private String severity;
}










