package com.acme.healthcare.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Request DTO for creating or updating visit patient diagnoses.
 */
@Data
public class VisitPatientDiagnosisRequest {
    
    @NotNull(message = "Visit ID is required")
    private Long visitId;
    
    @NotBlank(message = "Diagnosis code is required")
    private String code;
    
    private String description;
    
    private String status;
}










