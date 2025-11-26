package com.acme.healthcare.controller;

import com.acme.healthcare.exception.GlobalExceptionHandler;
import com.acme.healthcare.service.PatientVisitService;
import com.acme.healthcare.service.dto.PatientVisitRequest;
import com.acme.healthcare.service.dto.PatientVisitResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
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
 * Unit tests for {@link PatientVisitController}.
 */
@ExtendWith(MockitoExtension.class)
class PatientVisitControllerTest {

    @Mock
    private PatientVisitService visitService;

    @InjectMocks
    private PatientVisitController visitController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(visitController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void create_ShouldReturnCreatedVisit() throws Exception {
        PatientVisitResponse response = new PatientVisitResponse();
        response.setId(1L);
        response.setPatientId(10L);
        when(visitService.create(any(PatientVisitRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/visits")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "patientId": 10,
                      "physicianId": 20,
                      "visitTime": "2025-01-01T10:15:00",
                      "visitType": "Consultation"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1));

        ArgumentCaptor<PatientVisitRequest> captor = ArgumentCaptor.forClass(PatientVisitRequest.class);
        verify(visitService).create(captor.capture());
        assertThat(captor.getValue().getVisitTime()).isEqualTo(LocalDateTime.parse("2025-01-01T10:15:00"));
    }

    @Test
    void update_ShouldReturnUpdatedVisit() throws Exception {
        PatientVisitResponse response = new PatientVisitResponse();
        response.setId(2L);
        response.setVisitType("Follow-up");
        when(visitService.update(eq(2L), any(PatientVisitRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/visits/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "patientId": 10,
                      "physicianId": 20,
                      "visitTime": "2025-01-02T09:00:00",
                      "visitType": "Follow-up"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.visitType").value("Follow-up"));
    }

    @Test
    void findAll_ShouldReturnPagedVisits() throws Exception {
        PatientVisitResponse response = new PatientVisitResponse();
        response.setId(3L);
        Page<PatientVisitResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(visitService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/visits"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(3));
    }

    @Test
    void findByPatientId_ShouldReturnPagedVisits() throws Exception {
        PatientVisitResponse response = new PatientVisitResponse();
        response.setId(4L);
        Page<PatientVisitResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(visitService.findByPatientId(eq(11L), any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/visits/patient/11"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(4));
    }

    @Test
    void delete_ShouldInvokeService() throws Exception {
        mockMvc.perform(delete("/api/v1/visits/9"))
            .andExpect(status().isNoContent());

        verify(visitService).delete(9L);
    }
}











