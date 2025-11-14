package com.acme.healthcare.service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Response DTO for user-patient assignment operations.
 */
@Data
public class UserPatientAssignmentResponse {
    
    private Long id;
    private Long userId;
    private String userName;
    private Long patientId;
    private String patientName;
    private String assignmentRole;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdTime;
    private LocalDateTime modifiedTime;
    private String createdBy;
    private String lastModifiedBy;
}


