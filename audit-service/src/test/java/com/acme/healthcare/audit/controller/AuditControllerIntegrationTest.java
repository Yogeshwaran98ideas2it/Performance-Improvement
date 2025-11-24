package com.acme.healthcare.audit.controller;

import com.acme.healthcare.audit.AuditServiceApplication;
import com.acme.healthcare.common.dto.AuditEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the audit REST endpoints.
 */
@SpringBootTest(classes = AuditServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuditControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void publishAndFetchAuditRecord() throws Exception {
        AuditEventDto dto = AuditEventDto.builder()
            .entityType("Patient")
            .entityId(42L)
            .action("UPDATED")
            .username("integration-user")
            .tenantId("tenant-1")
            .correlationId("corr-123")
            .timestamp(Instant.now())
            .details(Map.of("field", "value"))
            .build();

        mockMvc.perform(post("/api/v1/audit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dto)))
            .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/audit/Patient/42"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].entityType").value("Patient"))
            .andExpect(jsonPath("$.content[0].entityId").value(42));

        mockMvc.perform(get("/api/v1/audit/entity/Patient"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].entityType").value("Patient"));

        mockMvc.perform(get("/api/v1/audit/user/integration-user"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].username").value("integration-user"));
    }
}


