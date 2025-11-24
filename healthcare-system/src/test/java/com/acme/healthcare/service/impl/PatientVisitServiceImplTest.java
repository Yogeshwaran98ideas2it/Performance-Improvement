package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.repository.PatientRepository;
import com.acme.healthcare.domain.repository.PatientVisitRepository;
import com.acme.healthcare.domain.repository.UserRepository;
import com.acme.healthcare.exception.ResourceNotFoundException;
import com.acme.healthcare.mapper.PatientVisitMapper;
import com.acme.healthcare.service.dto.PatientVisitRequest;
import com.acme.healthcare.service.dto.PatientVisitResponse;
import java.time.LocalDateTime;
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
 * Unit tests for {@link PatientVisitServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class PatientVisitServiceImplTest {

    @Mock
    private PatientVisitRepository visitRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientVisitMapper mapper;

    @InjectMocks
    private PatientVisitServiceImpl service;

    private PatientVisitRequest request;
    private Patient patient;
    private User physician;
    private PatientVisit visit;
    private PatientVisitResponse response;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);

        physician = new User();
        physician.setId(2L);

        request = new PatientVisitRequest();
        request.setPatientId(1L);
        request.setPhysicianId(2L);
        request.setVisitTime(LocalDateTime.now());
        request.setVisitType("Consultation");

        visit = new PatientVisit();
        visit.setId(10L);
        visit.setPatient(patient);
        visit.setPhysician(physician);

        response = new PatientVisitResponse();
        response.setId(10L);
        response.setPatientId(1L);
    }

    @Test
    void create_ShouldPersistVisit() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(userRepository.findById(2L)).thenReturn(Optional.of(physician));
        when(mapper.toEntity(request)).thenReturn(visit);
        when(visitRepository.save(visit)).thenReturn(visit);
        when(mapper.toResponse(visit)).thenReturn(response);

        PatientVisitResponse result = service.create(request);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(visitRepository, times(1)).save(visit);
    }

    @Test
    void create_WithMissingPatient_ShouldThrow() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(request));
        verify(visitRepository, never()).save(any());
    }

    @Test
    void update_ShouldMergeAndReturn() {
        request.setPatientId(null); // Don't update patient
        when(userRepository.findById(2L)).thenReturn(Optional.of(physician));
        when(visitRepository.findById(10L)).thenReturn(Optional.of(visit));
        when(visitRepository.save(visit)).thenReturn(visit);
        when(mapper.toResponse(visit)).thenReturn(response);

        PatientVisitResponse result = service.update(10L, request);

        assertNotNull(result);
        verify(visitRepository, times(1)).save(visit);
    }

    @Test
    void findById_ShouldReturnResponse() {
        when(visitRepository.findById(10L)).thenReturn(Optional.of(visit));
        when(mapper.toResponse(visit)).thenReturn(response);

        PatientVisitResponse result = service.findById(10L);

        assertEquals(10L, result.getId());
    }

    @Test
    void findAll_ShouldReturnPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PatientVisit> page = new PageImpl<>(List.of(visit), pageable, 1);

        when(visitRepository.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(visit)).thenReturn(response);

        Page<PatientVisitResponse> result = service.findAll(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByPatientId_ShouldReturnPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<PatientVisit> page = new PageImpl<>(List.of(visit), pageable, 1);

        when(visitRepository.findByPatientId(1L, pageable)).thenReturn(page);
        when(mapper.toResponse(visit)).thenReturn(response);

        Page<PatientVisitResponse> result = service.findByPatientId(1L, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void delete_WhenExists_ShouldInvokeRepository() {
        when(visitRepository.existsById(10L)).thenReturn(true);

        service.delete(10L);

        verify(visitRepository, times(1)).deleteById(10L);
    }

    @Test
    void delete_WhenMissing_ShouldThrow() {
        when(visitRepository.existsById(10L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.delete(10L));
        verify(visitRepository, never()).deleteById(anyLong());
    }
}

