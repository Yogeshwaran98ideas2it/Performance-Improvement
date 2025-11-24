package com.acme.healthcare.audit.domain;

import com.acme.healthcare.common.dto.AuditEventDto;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Domain tests for {@link AuditRecord}.
 */
class AuditRecordTest {

    @Test
    void settersShouldMutateState() {
        AuditRecord record = new AuditRecord();
        record.setId(10L);
        record.setEntityType("USER");
        record.setEntityId(5L);
        record.setAction("CREATED");
        record.setUsername("auditor");
        record.setOccurredAt(Instant.now());
        record.setTenantId("tenant-1");
        record.setCorrelationId("corr");
        Map<String, String> attributes = new HashMap<>();
        record.setAttributes(attributes);

        assertThat(record.getId()).isEqualTo(10L);
        assertThat(record.getAttributes()).isSameAs(attributes);
    }

    @Test
    void fromDto_ShouldPopulateFields() {
        AuditEventDto dto = AuditEventDto.builder()
            .entityType("PATIENT")
            .entityId(15L)
            .action("UPDATED")
            .username("system")
            .tenantId("tenant-1")
            .timestamp(Instant.now())
            .details(Map.of("key", "value"))
            .build();

        AuditRecord record = AuditRecord.fromDto(dto);

        assertThat(record.getEntityType()).isEqualTo("PATIENT");
        assertThat(record.getAttributes()).containsEntry("key", "value");
    }
}









