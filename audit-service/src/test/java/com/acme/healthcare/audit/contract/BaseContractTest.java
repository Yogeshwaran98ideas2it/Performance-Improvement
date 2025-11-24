package com.acme.healthcare.audit.contract;

import com.acme.healthcare.audit.AuditServiceApplication;
import com.acme.healthcare.audit.domain.AuditRecord;
import com.acme.healthcare.audit.domain.AuditRecordRepository;
import com.acme.healthcare.common.dto.AuditEventDto;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Base class used by Spring Cloud Contract generated tests.
 */
@SpringBootTest(classes = AuditServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class BaseContractTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private AuditRecordRepository repository;

    @BeforeEach
    void seedData() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        repository.deleteAll();
        AuditEventDto dto = AuditEventDto.builder()
            .entityType("Patient")
            .entityId(100L)
            .action("CREATED")
            .username("contract-user")
            .tenantId("tenant-demo")
            .correlationId("contract-correlation")
            .timestamp(Instant.parse("2025-01-01T00:00:00Z"))
            .details(Map.of("field", "value"))
            .build();
        repository.save(AuditRecord.fromDto(dto));
    }
}

