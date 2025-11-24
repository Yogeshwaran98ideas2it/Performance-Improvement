package com.acme.healthcare.service.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Response DTO for visit patient diagnosis operations.
 */
@Data
public class VisitPatientDiagnosisResponse {
    
    private Long id;
    private Long visitId;
    private String code;
    private String description;
    private String status;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private String createdBy;
    private String lastModifiedBy;
}










