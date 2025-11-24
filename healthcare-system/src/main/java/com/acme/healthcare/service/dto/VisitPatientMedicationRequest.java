package com.acme.healthcare.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO for creating or updating visit patient medications.
 */
@Data
public class VisitPatientMedicationRequest {
    
    @NotNull(message = "Visit ID is required")
    private Long visitId;
    
    @NotBlank(message = "Medication name is required")
    private String medicationName;
    
    private String dosage;
    
    private String frequency;
    
    private String route;
    
    private String instructions;
}










