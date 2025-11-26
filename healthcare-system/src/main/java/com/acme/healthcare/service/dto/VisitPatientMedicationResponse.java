package com.acme.healthcare.service.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Response DTO for visit patient medication operations.
 */
@Data
public class VisitPatientMedicationResponse {
    
    private Long id;
    private Long visitId;
    private String medicationName;
    private String dosage;
    private String frequency;
    private String route;
    private String instructions;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private String createdBy;
    private String lastModifiedBy;
}











