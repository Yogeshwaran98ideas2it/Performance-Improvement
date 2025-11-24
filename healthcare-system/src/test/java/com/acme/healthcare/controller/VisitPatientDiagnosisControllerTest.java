package com.acme.healthcare.controller;

import com.acme.healthcare.exception.GlobalExceptionHandler;
import com.acme.healthcare.service.VisitPatientDiagnosisService;
import com.acme.healthcare.service.dto.VisitPatientDiagnosisRequest;
import com.acme.healthcare.service.dto.VisitPatientDiagnosisResponse;
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
 * Unit tests for {@link VisitPatientDiagnosisController}.
 */
@ExtendWith(MockitoExtension.class)
class VisitPatientDiagnosisControllerTest {

    @Mock
    private VisitPatientDiagnosisService diagnosisService;

    @InjectMocks
    private VisitPatientDiagnosisController diagnosisController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(diagnosisController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void create_ShouldReturnCreatedDiagnosis() throws Exception {
        VisitPatientDiagnosisResponse response = new VisitPatientDiagnosisResponse();
        response.setId(1L);
        response.setCode("E11.9");
        when(diagnosisService.create(any(VisitPatientDiagnosisRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/diagnoses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "visitId": 10,
                      "code": "E11.9",
                      "description": "Type 2 diabetes"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value("E11.9"));

        ArgumentCaptor<VisitPatientDiagnosisRequest> captor = ArgumentCaptor.forClass(VisitPatientDiagnosisRequest.class);
        verify(diagnosisService).create(captor.capture());
        assertThat(captor.getValue().getVisitId()).isEqualTo(10L);
    }

    @Test
    void update_ShouldReturnUpdatedDiagnosis() throws Exception {
        VisitPatientDiagnosisResponse response = new VisitPatientDiagnosisResponse();
        response.setId(2L);
        response.setStatus("RESOLVED");
        when(diagnosisService.update(eq(2L), any(VisitPatientDiagnosisRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/diagnoses/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "visitId": 10,
                      "code": "E11.9",
                      "status": "RESOLVED"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("RESOLVED"));
    }

    @Test
    void findAll_ShouldReturnPagedDiagnoses() throws Exception {
        VisitPatientDiagnosisResponse response = new VisitPatientDiagnosisResponse();
        response.setId(3L);
        Page<VisitPatientDiagnosisResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(diagnosisService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/diagnoses"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(3));
    }

    @Test
    void delete_ShouldInvokeService() throws Exception {
        mockMvc.perform(delete("/api/v1/diagnoses/5"))
            .andExpect(status().isNoContent());

        verify(diagnosisService).delete(5L);
    }
}










