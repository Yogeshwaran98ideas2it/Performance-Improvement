package com.acme.healthcare.service.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

/**
 * Request DTO for creating or updating user-patient assignments.
 */
@Data
public class UserPatientAssignmentRequest {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    
    private String assignmentRole;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
}







