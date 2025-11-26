package com.acme.healthcare.controller;

import com.acme.healthcare.exception.GlobalExceptionHandler;
import com.acme.healthcare.service.VisitPatientMedicationService;
import com.acme.healthcare.service.dto.VisitPatientMedicationRequest;
import com.acme.healthcare.service.dto.VisitPatientMedicationResponse;
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
 * Unit tests for {@link VisitPatientMedicationController}.
 */
@ExtendWith(MockitoExtension.class)
class VisitPatientMedicationControllerTest {

    @Mock
    private VisitPatientMedicationService medicationService;

    @InjectMocks
    private VisitPatientMedicationController medicationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(medicationController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void create_ShouldReturnCreatedMedication() throws Exception {
        VisitPatientMedicationResponse response = new VisitPatientMedicationResponse();
        response.setId(1L);
        response.setMedicationName("Aspirin");
        when(medicationService.create(any(VisitPatientMedicationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/medications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "visitId": 10,
                      "medicationName": "Aspirin",
                      "dosage": "100mg"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.medicationName").value("Aspirin"));

        ArgumentCaptor<VisitPatientMedicationRequest> captor = ArgumentCaptor.forClass(VisitPatientMedicationRequest.class);
        verify(medicationService).create(captor.capture());
        assertThat(captor.getValue().getVisitId()).isEqualTo(10L);
    }

    @Test
    void update_ShouldReturnUpdatedMedication() throws Exception {
        VisitPatientMedicationResponse response = new VisitPatientMedicationResponse();
        response.setId(2L);
        response.setFrequency("Twice daily");
        when(medicationService.update(eq(2L), any(VisitPatientMedicationRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/medications/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "visitId": 10,
                      "medicationName": "Aspirin",
                      "dosage": "100mg",
                      "frequency": "Twice daily"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.frequency").value("Twice daily"));
    }

    @Test
    void findAll_ShouldReturnPagedMedications() throws Exception {
        VisitPatientMedicationResponse response = new VisitPatientMedicationResponse();
        response.setId(3L);
        Page<VisitPatientMedicationResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(medicationService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/medications"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(3));
    }

    @Test
    void delete_ShouldInvokeService() throws Exception {
        mockMvc.perform(delete("/api/v1/medications/5"))
            .andExpect(status().isNoContent());

        verify(medicationService).delete(5L);
    }
}











