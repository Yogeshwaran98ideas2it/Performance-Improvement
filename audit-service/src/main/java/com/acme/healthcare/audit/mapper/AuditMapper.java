package com.acme.healthcare.audit.mapper;

import com.acme.healthcare.audit.domain.AuditRecord;
import com.acme.healthcare.common.dto.AuditRecordResponse;
import java.util.HashMap;
import org.springframework.stereotype.Component;

/**
 * Maps audit entities to DTOs.
 */
@Component
public class AuditMapper {

    /**
     * Converts a record entity to a DTO.
     *
     * @param record audit record
     * @return response DTO
     */
    public AuditRecordResponse toResponse(final AuditRecord record) {
        return AuditRecordResponse.builder()
            .id(record.getId())
            .entityType(record.getEntityType())
            .entityId(record.getEntityId())
            .action(record.getAction())
            .username(record.getUsername())
            .createdAt(record.getOccurredAt())
            .tenantId(record.getTenantId())
            .correlationId(record.getCorrelationId())
            .details(new HashMap<>(record.getAttributes()))
            .build();
    }
}









