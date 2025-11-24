package com.acme.healthcare.controller;

import com.acme.healthcare.exception.GlobalExceptionHandler;
import com.acme.healthcare.service.UserPatientAssignmentService;
import com.acme.healthcare.service.dto.UserPatientAssignmentRequest;
import com.acme.healthcare.service.dto.UserPatientAssignmentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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
 * Unit tests for {@link UserPatientAssignmentController}.
 */
@ExtendWith(MockitoExtension.class)
class UserPatientAssignmentControllerTest {

    @Mock
    private UserPatientAssignmentService assignmentService;

    @InjectMocks
    private UserPatientAssignmentController assignmentController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(assignmentController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void create_ShouldReturnCreatedAssignment() throws Exception {
        UserPatientAssignmentResponse response = new UserPatientAssignmentResponse();
        response.setId(1L);
        response.setUserId(11L);
        response.setPatientId(22L);
        when(assignmentService.create(any(UserPatientAssignmentRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/assignments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "userId": 11,
                      "patientId": 22,
                      "assignmentRole": "Primary",
                      "startDate": "2025-01-01"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1));

        ArgumentCaptor<UserPatientAssignmentRequest> captor = ArgumentCaptor.forClass(UserPatientAssignmentRequest.class);
        verify(assignmentService).create(captor.capture());
        assertThat(captor.getValue().getUserId()).isEqualTo(11L);
        assertThat(captor.getValue().getStartDate()).isEqualTo(LocalDate.parse("2025-01-01"));
    }

    @Test
    void update_ShouldReturnUpdatedAssignment() throws Exception {
        UserPatientAssignmentResponse response = new UserPatientAssignmentResponse();
        response.setId(5L);
        response.setAssignmentRole("Updated");
        when(assignmentService.update(eq(5L), any(UserPatientAssignmentRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/assignments/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "userId": 11,
                      "patientId": 22,
                      "assignmentRole": "Updated"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.assignmentRole").value("Updated"));
    }

    @Test
    void findAll_ShouldReturnPagedAssignments() throws Exception {
        UserPatientAssignmentResponse response = new UserPatientAssignmentResponse();
        response.setId(2L);
        Page<UserPatientAssignmentResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(assignmentService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/assignments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(2));
    }

    @Test
    void findByUserId_ShouldReturnPagedAssignments() throws Exception {
        UserPatientAssignmentResponse response = new UserPatientAssignmentResponse();
        response.setId(3L);
        Page<UserPatientAssignmentResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(assignmentService.findByUserId(eq(99L), any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/assignments/user/99"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(3));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/assignments/8"))
            .andExpect(status().isNoContent());

        verify(assignmentService).delete(8L);
    }
}










