package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.entity.VisitPatientAllergy;
import com.acme.healthcare.domain.repository.PatientVisitRepository;
import com.acme.healthcare.domain.repository.VisitPatientAllergyRepository;
import com.acme.healthcare.exception.ResourceNotFoundException;
import com.acme.healthcare.mapper.VisitPatientAllergyMapper;
import com.acme.healthcare.service.dto.VisitPatientAllergyRequest;
import com.acme.healthcare.service.dto.VisitPatientAllergyResponse;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link VisitPatientAllergyServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class VisitPatientAllergyServiceImplTest {

    @Mock
    private VisitPatientAllergyRepository allergyRepository;

    @Mock
    private PatientVisitRepository visitRepository;

    @Mock
    private VisitPatientAllergyMapper mapper;

    @InjectMocks
    private VisitPatientAllergyServiceImpl service;

    private VisitPatientAllergyRequest request;
    private PatientVisit visit;
    private VisitPatientAllergy allergy;
    private VisitPatientAllergyResponse response;

    @BeforeEach
    void setUp() {
        visit = new PatientVisit();
        visit.setId(10L);

        request = new VisitPatientAllergyRequest();
        request.setVisitId(10L);
        request.setAllergen("Peanuts");
        request.setReaction("Hives");
        request.setSeverity("Moderate");

        allergy = new VisitPatientAllergy();
        allergy.setId(30L);
        allergy.setVisit(visit);
        allergy.setAllergen("Peanuts");

        response = new VisitPatientAllergyResponse();
        response.setId(30L);
        response.setAllergen("Peanuts");
    }

    @Test
    void create_ShouldPersistAllergy() {
        when(visitRepository.findById(10L)).thenReturn(Optional.of(visit));
        when(mapper.toEntity(request)).thenReturn(allergy);
        when(allergyRepository.save(allergy)).thenReturn(allergy);
        when(mapper.toResponse(allergy)).thenReturn(response);

        VisitPatientAllergyResponse result = service.create(request);

        assertNotNull(result);
        assertEquals("Peanuts", result.getAllergen());
        verify(allergyRepository, times(1)).save(allergy);
    }

    @Test
    void create_WithMissingVisit_ShouldThrow() {
        when(visitRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(request));
        verify(allergyRepository, never()).save(any());
    }

    @Test
    void update_ShouldMergeAndReturn() {
        when(visitRepository.findById(10L)).thenReturn(Optional.of(visit));
        when(allergyRepository.findById(30L)).thenReturn(Optional.of(allergy));
        when(allergyRepository.save(allergy)).thenReturn(allergy);
        when(mapper.toResponse(allergy)).thenReturn(response);

        VisitPatientAllergyResponse result = service.update(30L, request);

        assertNotNull(result);
        verify(allergyRepository, times(1)).save(allergy);
    }

    @Test
    void findById_ShouldReturnResponse() {
        when(allergyRepository.findById(30L)).thenReturn(Optional.of(allergy));
        when(mapper.toResponse(allergy)).thenReturn(response);

        VisitPatientAllergyResponse result = service.findById(30L);

        assertEquals(30L, result.getId());
    }

    @Test
    void findAll_ShouldReturnPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<VisitPatientAllergy> page = new PageImpl<>(List.of(allergy), pageable, 1);

        when(allergyRepository.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(allergy)).thenReturn(response);

        Page<VisitPatientAllergyResponse> result = service.findAll(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByVisitId_ShouldReturnPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<VisitPatientAllergy> page = new PageImpl<>(List.of(allergy), pageable, 1);

        when(allergyRepository.findByVisitId(10L, pageable)).thenReturn(page);
        when(mapper.toResponse(allergy)).thenReturn(response);

        Page<VisitPatientAllergyResponse> result = service.findByVisitId(10L, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void delete_WhenExists_ShouldInvokeRepository() {
        when(allergyRepository.existsById(30L)).thenReturn(true);

        service.delete(30L);

        verify(allergyRepository, times(1)).deleteById(30L);
    }

    @Test
    void delete_WhenMissing_ShouldThrow() {
        when(allergyRepository.existsById(30L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.delete(30L));
        verify(allergyRepository, never()).deleteById(anyLong());
    }
}


