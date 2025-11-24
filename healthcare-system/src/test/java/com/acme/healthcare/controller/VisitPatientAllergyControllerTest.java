package com.acme.healthcare.controller;

import com.acme.healthcare.exception.GlobalExceptionHandler;
import com.acme.healthcare.service.VisitPatientAllergyService;
import com.acme.healthcare.service.dto.VisitPatientAllergyRequest;
import com.acme.healthcare.service.dto.VisitPatientAllergyResponse;
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
 * Unit tests for {@link VisitPatientAllergyController}.
 */
@ExtendWith(MockitoExtension.class)
class VisitPatientAllergyControllerTest {

    @Mock
    private VisitPatientAllergyService allergyService;

    @InjectMocks
    private VisitPatientAllergyController allergyController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(allergyController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
    }

    @Test
    void create_ShouldReturnCreatedAllergy() throws Exception {
        VisitPatientAllergyResponse response = new VisitPatientAllergyResponse();
        response.setId(1L);
        response.setAllergen("Peanuts");
        when(allergyService.create(any(VisitPatientAllergyRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/allergies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "visitId": 10,
                      "allergen": "Peanuts",
                      "reaction": "Hives"
                    }
                    """))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.allergen").value("Peanuts"));

        ArgumentCaptor<VisitPatientAllergyRequest> captor = ArgumentCaptor.forClass(VisitPatientAllergyRequest.class);
        verify(allergyService).create(captor.capture());
        assertThat(captor.getValue().getVisitId()).isEqualTo(10L);
    }

    @Test
    void update_ShouldReturnUpdatedAllergy() throws Exception {
        VisitPatientAllergyResponse response = new VisitPatientAllergyResponse();
        response.setId(2L);
        response.setReaction("Mild");
        when(allergyService.update(eq(2L), any(VisitPatientAllergyRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/allergies/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "visitId": 10,
                      "allergen": "Peanuts",
                      "reaction": "Mild"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.reaction").value("Mild"));
    }

    @Test
    void findAll_ShouldReturnPagedAllergies() throws Exception {
        VisitPatientAllergyResponse response = new VisitPatientAllergyResponse();
        response.setId(3L);
        Page<VisitPatientAllergyResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 20), 1);
        when(allergyService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/allergies"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id").value(3));
    }

    @Test
    void delete_ShouldInvokeService() throws Exception {
        mockMvc.perform(delete("/api/v1/allergies/5"))
            .andExpect(status().isNoContent());

        verify(allergyService).delete(5L);
    }
}










