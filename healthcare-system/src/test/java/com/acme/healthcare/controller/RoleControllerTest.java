package com.acme.healthcare.controller;

import com.acme.healthcare.exception.GlobalExceptionHandler;
import com.acme.healthcare.service.RoleService;
import com.acme.healthcare.service.dto.RoleRequest;
import com.acme.healthcare.service.dto.RoleResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
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
 * Unit tests for {@link RoleController}.
 */
@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void create_ShouldReturnCreatedRole() throws Exception {
        RoleResponse response = new RoleResponse();
        response.setId(1L);
        response.setName("ADMIN");
        when(roleService.create(any(RoleRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "ADMIN",
                      "description": "Administrator",
                      "permissionIds": [1,2]
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("ADMIN"));

        ArgumentCaptor<RoleRequest> captor = ArgumentCaptor.forClass(RoleRequest.class);
        verify(roleService).create(captor.capture());
        assertThat(captor.getValue().getPermissionIds()).containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void update_ShouldReturnUpdatedRole() throws Exception {
        RoleResponse response = new RoleResponse();
        response.setId(2L);
        response.setName("MANAGER");
        when(roleService.update(eq(2L), any(RoleRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/roles/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "name": "MANAGER",
                      "description": "Updated",
                      "permissionIds": [3]
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("MANAGER"));
    }

    @Test
    void list_ShouldReturnPagedRoles() throws Exception {
        RoleResponse response = new RoleResponse();
        response.setId(3L);
        response.setName("USER");
        Page<RoleResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(roleService.list(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/roles"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("USER"));
    }

    @Test
    void get_ShouldReturnRole() throws Exception {
        RoleResponse response = new RoleResponse();
        response.setId(4L);
        response.setName("VIEWER");
        when(roleService.get(4L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/roles/4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("VIEWER"));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/roles/5"))
            .andExpect(status().isNoContent());

        verify(roleService).delete(5L);
    }
}










