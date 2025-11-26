package com.acme.healthcare.service.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Response DTO for visit patient allergy operations.
 */
@Data
public class VisitPatientAllergyResponse {
    
    private Long id;
    private Long visitId;
    private String allergen;
    private String reaction;
    private String severity;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private String createdBy;
    private String lastModifiedBy;
}











