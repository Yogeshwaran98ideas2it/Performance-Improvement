package com.acme.healthcare.controller;

import com.acme.healthcare.exception.GlobalExceptionHandler;
import com.acme.healthcare.service.RolePermissionService;
import com.acme.healthcare.service.dto.RolePermissionRequest;
import com.acme.healthcare.service.dto.RolePermissionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link RolePermissionController}.
 */
@ExtendWith(MockitoExtension.class)
class RolePermissionControllerTest {

    @Mock
    private RolePermissionService rolePermissionService;

    @InjectMocks
    private RolePermissionController rolePermissionController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rolePermissionController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void create_ShouldReturnCreatedPermission() throws Exception {
        RolePermissionResponse response = new RolePermissionResponse();
        response.setId(1L);
        response.setCode("PATIENT_READ");
        when(rolePermissionService.create(any(RolePermissionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "code": "PATIENT_READ",
                      "description": "Read patient"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value("PATIENT_READ"));

        ArgumentCaptor<RolePermissionRequest> captor = ArgumentCaptor.forClass(RolePermissionRequest.class);
        verify(rolePermissionService).create(captor.capture());
        assertThat(captor.getValue().getCode()).isEqualTo("PATIENT_READ");
    }

    @Test
    void update_ShouldReturnUpdatedPermission() throws Exception {
        RolePermissionResponse response = new RolePermissionResponse();
        response.setId(2L);
        response.setCode("PATIENT_UPDATE");
        when(rolePermissionService.update(eq(2L), any(RolePermissionRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/permissions/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "code": "PATIENT_UPDATE",
                      "description": "Update patient"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("PATIENT_UPDATE"));
    }

    @Test
    void list_ShouldReturnPagedPermissions() throws Exception {
        RolePermissionResponse response = new RolePermissionResponse();
        response.setId(3L);
        response.setCode("USER_READ");
        Page<RolePermissionResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(rolePermissionService.list(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/permissions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].code").value("USER_READ"));
    }

    @Test
    void get_ShouldReturnPermission() throws Exception {
        RolePermissionResponse response = new RolePermissionResponse();
        response.setId(4L);
        response.setCode("AUDIT_READ");
        when(rolePermissionService.get(4L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/permissions/4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("AUDIT_READ"));
    }

    @Test
    void delete_ShouldInvokeService() throws Exception {
        mockMvc.perform(delete("/api/v1/permissions/5"))
            .andExpect(status().isNoContent());

        verify(rolePermissionService).delete(5L);
    }
}


