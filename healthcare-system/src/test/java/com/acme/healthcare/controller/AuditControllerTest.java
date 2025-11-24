package com.acme.healthcare.controller;

import com.acme.healthcare.exception.GlobalExceptionHandler;
import com.acme.healthcare.service.AuditService;
import com.acme.healthcare.service.dto.AuditResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link AuditController}.
 */
@ExtendWith(MockitoExtension.class)
class AuditControllerTest {

    @Mock
    private AuditService auditService;

    @InjectMocks
    private AuditController auditController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(auditController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void getAuditInfo_ShouldReturnStatusOk() throws Exception {
        AuditResponse response = new AuditResponse();
        response.setEntityId(5L);
        response.setEntityType("Patient");
        when(auditService.getAuditInfo("patient", 5L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/audit/patient/5"))
            .andExpect(status().isOk());

        verify(auditService).getAuditInfo("patient", 5L);
    }

    @Test
    void getAuditInfoByEntityType_ShouldReturnStatusOk() throws Exception {
        AuditResponse response = new AuditResponse();
        response.setEntityType("user");
        Page<AuditResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(auditService.getAuditInfoByEntityType(eq("user"), any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/audit/entity/user"))
            .andExpect(status().isOk());

        verify(auditService).getAuditInfoByEntityType(eq("user"), any());
    }

    @Test
    void getAuditInfoByUser_ShouldReturnStatusOk() throws Exception {
        AuditResponse response = new AuditResponse();
        response.setEntityType("user");
        Page<AuditResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(auditService.getAuditInfoByUser(eq("auditor"), any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/audit/user/auditor"))
            .andExpect(status().isOk());

        verify(auditService).getAuditInfoByUser(eq("auditor"), any());
    }
}


