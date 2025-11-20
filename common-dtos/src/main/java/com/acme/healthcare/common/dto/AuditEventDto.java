package com.acme.healthcare.common.dto;

import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an audit event propagated between services.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditEventDto {

    private String entityType;
    private Long entityId;
    private String action;
    private String username;
    private Instant timestamp;
    private Map<String, Object> details;
    private String tenantId;
    private String correlationId;
}









