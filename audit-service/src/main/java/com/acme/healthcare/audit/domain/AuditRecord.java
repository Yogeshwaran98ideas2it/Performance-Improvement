package com.acme.healthcare.audit.domain;

import com.acme.healthcare.common.dto.AuditEventDto;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AuditRecord stores immutable audit trail entries.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "audit_records")
public class AuditRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String entityType;

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false, length = 80)
    private String action;

    @Column(nullable = false, length = 120)
    private String username;

    @Column(nullable = false)
    private Instant occurredAt;

    @Column(length = 64)
    private String tenantId;

    @Column(length = 64)
    private String correlationId;

    @ElementCollection
    @CollectionTable(name = "audit_record_attributes")
    @MapKeyColumn(name = "attribute_key")
    @Column(name = "attribute_value", length = 500)
    private Map<String, String> attributes = new HashMap<>();

    /**
     * Factory method to build a record from a DTO.
     *
     * @param dto inbound DTO
     * @return audit record
     */
    public static AuditRecord fromDto(final AuditEventDto dto) {
        AuditRecord record = new AuditRecord();
        record.setEntityType(dto.getEntityType());
        record.setEntityId(dto.getEntityId());
        record.setAction(dto.getAction());
        record.setUsername(dto.getUsername());
        record.setOccurredAt(dto.getTimestamp());
        record.setTenantId(dto.getTenantId());
        record.setCorrelationId(dto.getCorrelationId());
        if (dto.getDetails() != null) {
            dto.getDetails().forEach((key, value) ->
                record.getAttributes().put(key, value != null ? value.toString() : null));
        }
        return record;
    }
}










