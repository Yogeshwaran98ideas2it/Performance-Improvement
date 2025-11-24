package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.entity.VisitPatientMedication;
import com.acme.healthcare.domain.repository.PatientVisitRepository;
import com.acme.healthcare.domain.repository.VisitPatientMedicationRepository;
import com.acme.healthcare.exception.ResourceNotFoundException;
import com.acme.healthcare.mapper.VisitPatientMedicationMapper;
import com.acme.healthcare.service.dto.VisitPatientMedicationRequest;
import com.acme.healthcare.service.dto.VisitPatientMedicationResponse;
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
 * Unit tests for {@link VisitPatientMedicationServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class VisitPatientMedicationServiceImplTest {

    @Mock
    private VisitPatientMedicationRepository medicationRepository;

    @Mock
    private PatientVisitRepository visitRepository;

    @Mock
    private VisitPatientMedicationMapper mapper;

    @InjectMocks
    private VisitPatientMedicationServiceImpl service;

    private VisitPatientMedicationRequest request;
    private PatientVisit visit;
    private VisitPatientMedication medication;
    private VisitPatientMedicationResponse response;

    @BeforeEach
    void setUp() {
        visit = new PatientVisit();
        visit.setId(10L);

        request = new VisitPatientMedicationRequest();
        request.setVisitId(10L);
        request.setMedicationName("Aspirin");
        request.setDosage("100mg");
        request.setFrequency("Once daily");

        medication = new VisitPatientMedication();
        medication.setId(40L);
        medication.setVisit(visit);
        medication.setMedicationName("Aspirin");

        response = new VisitPatientMedicationResponse();
        response.setId(40L);
        response.setMedicationName("Aspirin");
    }

    @Test
    void create_ShouldPersistMedication() {
        when(visitRepository.findById(10L)).thenReturn(Optional.of(visit));
        when(mapper.toEntity(request)).thenReturn(medication);
        when(medicationRepository.save(medication)).thenReturn(medication);
        when(mapper.toResponse(medication)).thenReturn(response);

        VisitPatientMedicationResponse result = service.create(request);

        assertNotNull(result);
        assertEquals("Aspirin", result.getMedicationName());
        verify(medicationRepository, times(1)).save(medication);
    }

    @Test
    void create_WithMissingVisit_ShouldThrow() {
        when(visitRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(request));
        verify(medicationRepository, never()).save(any());
    }

    @Test
    void update_ShouldMergeAndReturn() {
        when(visitRepository.findById(10L)).thenReturn(Optional.of(visit));
        when(medicationRepository.findById(40L)).thenReturn(Optional.of(medication));
        when(medicationRepository.save(medication)).thenReturn(medication);
        when(mapper.toResponse(medication)).thenReturn(response);

        VisitPatientMedicationResponse result = service.update(40L, request);

        assertNotNull(result);
        verify(medicationRepository, times(1)).save(medication);
    }

    @Test
    void findById_ShouldReturnResponse() {
        when(medicationRepository.findById(40L)).thenReturn(Optional.of(medication));
        when(mapper.toResponse(medication)).thenReturn(response);

        VisitPatientMedicationResponse result = service.findById(40L);

        assertEquals(40L, result.getId());
    }

    @Test
    void findAll_ShouldReturnPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<VisitPatientMedication> page = new PageImpl<>(List.of(medication), pageable, 1);

        when(medicationRepository.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(medication)).thenReturn(response);

        Page<VisitPatientMedicationResponse> result = service.findAll(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByVisitId_ShouldReturnPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<VisitPatientMedication> page = new PageImpl<>(List.of(medication), pageable, 1);

        when(medicationRepository.findByVisitId(10L, pageable)).thenReturn(page);
        when(mapper.toResponse(medication)).thenReturn(response);

        Page<VisitPatientMedicationResponse> result = service.findByVisitId(10L, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void delete_WhenExists_ShouldInvokeRepository() {
        when(medicationRepository.existsById(40L)).thenReturn(true);

        service.delete(40L);

        verify(medicationRepository, times(1)).deleteById(40L);
    }

    @Test
    void delete_WhenMissing_ShouldThrow() {
        when(medicationRepository.existsById(40L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.delete(40L));
        verify(medicationRepository, never()).deleteById(anyLong());
    }
}


