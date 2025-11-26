package com.acme.healthcare.controller;

import com.acme.healthcare.exception.GlobalExceptionHandler;
import com.acme.healthcare.service.PatientService;
import com.acme.healthcare.service.dto.PatientRequest;
import com.acme.healthcare.service.dto.PatientResponse;
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
 * Unit tests for {@link PatientController}.
 */
@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(patientController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void create_ShouldPersistPatient() throws Exception {
        PatientResponse response = new PatientResponse();
        response.setId(1L);
        response.setFirstName("Jane");
        when(patientService.create(any(PatientRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "medicalRecordNumber": "MRN-1",
                      "firstName": "Jane",
                      "lastName": "Doe"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1));

        ArgumentCaptor<PatientRequest> captor = ArgumentCaptor.forClass(PatientRequest.class);
        verify(patientService).create(captor.capture());
        assertThat(captor.getValue().getMedicalRecordNumber()).isEqualTo("MRN-1");
    }

    @Test
    void update_ShouldReturnUpdatedPatient() throws Exception {
        PatientResponse response = new PatientResponse();
        response.setId(2L);
        response.setFirstName("Updated");
        when(patientService.update(eq(2L), any(PatientRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/patients/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "medicalRecordNumber": "MRN-2",
                      "firstName": "Updated",
                      "lastName": "Person"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    void get_ShouldReturnPatient() throws Exception {
        PatientResponse response = new PatientResponse();
        response.setId(3L);
        when(patientService.get(3L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/patients/3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void list_ShouldReturnPagedPatients() throws Exception {
        PatientResponse response = new PatientResponse();
        response.setId(4L);
        Page<PatientResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(patientService.list(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/patients?page=0&size=20"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(4));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/patients/5"))
            .andExpect(status().isNoContent());

        verify(patientService).delete(5L);
    }
}











