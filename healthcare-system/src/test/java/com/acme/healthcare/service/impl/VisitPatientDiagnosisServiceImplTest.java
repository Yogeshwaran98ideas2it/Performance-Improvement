package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.PatientVisit;
import com.acme.healthcare.domain.entity.VisitPatientDiagnosis;
import com.acme.healthcare.domain.repository.PatientVisitRepository;
import com.acme.healthcare.domain.repository.VisitPatientDiagnosisRepository;
import com.acme.healthcare.exception.ResourceNotFoundException;
import com.acme.healthcare.mapper.VisitPatientDiagnosisMapper;
import com.acme.healthcare.service.dto.VisitPatientDiagnosisRequest;
import com.acme.healthcare.service.dto.VisitPatientDiagnosisResponse;
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
 * Unit tests for {@link VisitPatientDiagnosisServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class VisitPatientDiagnosisServiceImplTest {

    @Mock
    private VisitPatientDiagnosisRepository diagnosisRepository;

    @Mock
    private PatientVisitRepository visitRepository;

    @Mock
    private VisitPatientDiagnosisMapper mapper;

    @InjectMocks
    private VisitPatientDiagnosisServiceImpl service;

    private VisitPatientDiagnosisRequest request;
    private PatientVisit visit;
    private VisitPatientDiagnosis diagnosis;
    private VisitPatientDiagnosisResponse response;

    @BeforeEach
    void setUp() {
        visit = new PatientVisit();
        visit.setId(10L);

        request = new VisitPatientDiagnosisRequest();
        request.setVisitId(10L);
        request.setCode("E11.9");
        request.setDescription("Type 2 diabetes");

        diagnosis = new VisitPatientDiagnosis();
        diagnosis.setId(20L);
        diagnosis.setVisit(visit);
        diagnosis.setCode("E11.9");

        response = new VisitPatientDiagnosisResponse();
        response.setId(20L);
        response.setCode("E11.9");
    }

    @Test
    void create_ShouldPersistDiagnosis() {
        when(visitRepository.findById(10L)).thenReturn(Optional.of(visit));
        when(mapper.toEntity(request)).thenReturn(diagnosis);
        when(diagnosisRepository.save(diagnosis)).thenReturn(diagnosis);
        when(mapper.toResponse(diagnosis)).thenReturn(response);

        VisitPatientDiagnosisResponse result = service.create(request);

        assertNotNull(result);
        assertEquals("E11.9", result.getCode());
        verify(diagnosisRepository, times(1)).save(diagnosis);
    }

    @Test
    void create_WithMissingVisit_ShouldThrow() {
        when(visitRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(request));
        verify(diagnosisRepository, never()).save(any());
    }

    @Test
    void update_ShouldMergeAndReturn() {
        when(visitRepository.findById(10L)).thenReturn(Optional.of(visit));
        when(diagnosisRepository.findById(20L)).thenReturn(Optional.of(diagnosis));
        when(diagnosisRepository.save(diagnosis)).thenReturn(diagnosis);
        when(mapper.toResponse(diagnosis)).thenReturn(response);

        VisitPatientDiagnosisResponse result = service.update(20L, request);

        assertNotNull(result);
        verify(diagnosisRepository, times(1)).save(diagnosis);
    }

    @Test
    void findById_ShouldReturnResponse() {
        when(diagnosisRepository.findById(20L)).thenReturn(Optional.of(diagnosis));
        when(mapper.toResponse(diagnosis)).thenReturn(response);

        VisitPatientDiagnosisResponse result = service.findById(20L);

        assertEquals(20L, result.getId());
    }

    @Test
    void findAll_ShouldReturnPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<VisitPatientDiagnosis> page = new PageImpl<>(List.of(diagnosis), pageable, 1);

        when(diagnosisRepository.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(diagnosis)).thenReturn(response);

        Page<VisitPatientDiagnosisResponse> result = service.findAll(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByVisitId_ShouldReturnPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<VisitPatientDiagnosis> page = new PageImpl<>(List.of(diagnosis), pageable, 1);

        when(diagnosisRepository.findByVisitId(10L, pageable)).thenReturn(page);
        when(mapper.toResponse(diagnosis)).thenReturn(response);

        Page<VisitPatientDiagnosisResponse> result = service.findByVisitId(10L, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void delete_WhenExists_ShouldInvokeRepository() {
        when(diagnosisRepository.existsById(20L)).thenReturn(true);

        service.delete(20L);

        verify(diagnosisRepository, times(1)).deleteById(20L);
    }

    @Test
    void delete_WhenMissing_ShouldThrow() {
        when(diagnosisRepository.existsById(20L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.delete(20L));
        verify(diagnosisRepository, never()).deleteById(anyLong());
    }
}


