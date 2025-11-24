package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.repository.PatientRepository;
import com.acme.healthcare.domain.repository.UserRepository;
import com.acme.healthcare.mapper.PatientMapper;
import com.acme.healthcare.service.dto.PatientRequest;
import com.acme.healthcare.service.dto.PatientResponse;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
 * Unit tests for {@link PatientServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private PatientRequest request;
    private PatientResponse response;
    private Patient persistedPatient;

    @BeforeEach
    void setUp() {
        request = new PatientRequest();
        request.setMedicalRecordNumber("MRN-123");
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setDateOfBirth(LocalDate.of(1988, 8, 8));
        request.setGender(com.acme.healthcare.domain.enums.UserGender.FEMALE);
        request.setSecondaryPhysicianIds(Set.of());
        request.setReferralPhysicianIds(Set.of());

        persistedPatient = new Patient();
        persistedPatient.setId(10L);
        persistedPatient.setMedicalRecordNumber("MRN-123");
        persistedPatient.setFirstName("Jane");
        persistedPatient.setLastName("Doe");

        response = new PatientResponse();
        response.setId(10L);
        response.setMedicalRecordNumber("MRN-123");
    }

    @Test
    void create_ShouldPersistPatient() {
        when(patientRepository.save(any(Patient.class))).thenReturn(persistedPatient);
        when(patientMapper.toResponse(persistedPatient)).thenReturn(response);

        PatientResponse result = patientService.create(request);

        assertNotNull(result);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void create_WithPrimaryPhysician_ShouldResolveReference() {
        User physician = new User();
        physician.setId(5L);
        request.setPrimaryPhysicianId(5L);

        when(userRepository.findById(5L)).thenReturn(Optional.of(physician));
        when(patientRepository.save(any(Patient.class))).thenReturn(persistedPatient);
        when(patientMapper.toResponse(persistedPatient)).thenReturn(response);

        PatientResponse result = patientService.create(request);

        assertEquals("MRN-123", result.getMedicalRecordNumber());
        verify(userRepository, times(1)).findById(5L);
    }

    @Test
    void create_WithMissingPrimaryPhysician_ShouldThrowEntityNotFound() {
        request.setPrimaryPhysicianId(5L);
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> patientService.create(request));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void update_ShouldMergeAndReturnResponse() {
        when(patientRepository.findById(10L)).thenReturn(Optional.of(persistedPatient));
        when(patientRepository.save(persistedPatient)).thenReturn(persistedPatient);
        when(patientMapper.toResponse(persistedPatient)).thenReturn(response);

        PatientResponse result = patientService.update(10L, request);

        assertNotNull(result);
        verify(patientRepository, times(1)).save(persistedPatient);
    }

    @Test
    void update_WhenPatientMissing_ShouldThrowEntityNotFound() {
        when(patientRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> patientService.update(10L, request));
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void get_ShouldReturnResponse() {
        when(patientRepository.findById(10L)).thenReturn(Optional.of(persistedPatient));
        when(patientMapper.toResponse(persistedPatient)).thenReturn(response);

        PatientResponse result = patientService.get(10L);

        assertEquals(10L, result.getId());
    }

    @Test
    void get_WhenMissing_ShouldThrowEntityNotFound() {
        when(patientRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> patientService.get(10L));
    }

    @Test
    void list_ShouldReturnPagedResult() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Patient> page = new PageImpl<>(List.of(persistedPatient), pageable, 1);

        when(patientRepository.findAll(pageable)).thenReturn(page);
        when(patientMapper.toResponse(persistedPatient)).thenReturn(response);

        Page<PatientResponse> result = patientService.list(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void delete_WhenExists_ShouldInvokeRepository() {
        when(patientRepository.existsById(10L)).thenReturn(true);

        patientService.delete(10L);

        verify(patientRepository, times(1)).deleteById(10L);
    }

    @Test
    void delete_WhenMissing_ShouldThrowEntityNotFound() {
        when(patientRepository.existsById(10L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> patientService.delete(10L));
        verify(patientRepository, never()).deleteById(anyLong());
    }
}

