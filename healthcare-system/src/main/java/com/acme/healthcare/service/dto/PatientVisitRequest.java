package com.acme.healthcare.service.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Request DTO for creating or updating patient visits.
 */
@Data
public class PatientVisitRequest {
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    private Long physicianId;
    
    @NotNull(message = "Visit time is required")
    private LocalDateTime visitTime;
    
    private String visitType;
    
    private String location;
    
    private String reason;
    
    private String notes;
}


