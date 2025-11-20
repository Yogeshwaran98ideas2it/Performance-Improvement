package com.acme.healthcare.common.dto;

import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response payload for audit record lookups.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditRecordResponse {

    private Long id;
    private String entityType;
    private Long entityId;
    private String action;
    private String username;
    private Instant createdAt;
    private String tenantId;
    private String correlationId;
    private Map<String, Object> details;
}


