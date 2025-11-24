package com.acme.healthcare.service.dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Response DTO for patient visit operations.
 */
@Data
public class PatientVisitResponse {
    
    private Long id;
    private Long patientId;
    private String patientName;
    private Long physicianId;
    private String physicianName;
    private LocalDateTime visitTime;
    private String visitType;
    private String location;
    private String reason;
    private String notes;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private String createdBy;
    private String lastModifiedBy;
}










