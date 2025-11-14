package com.acme.healthcare.service.dto;

import java.time.Instant;
import lombok.Data;

/**
 * Response DTO for audit information.
 */
@Data
public class AuditResponse {
    
    private String entityType;
    private Long entityId;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private Long version;
}


