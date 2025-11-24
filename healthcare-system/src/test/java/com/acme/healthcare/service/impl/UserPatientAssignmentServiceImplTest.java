package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.entity.UserPatientAssignment;
import com.acme.healthcare.domain.repository.PatientRepository;
import com.acme.healthcare.domain.repository.UserPatientAssignmentRepository;
import com.acme.healthcare.domain.repository.UserRepository;
import com.acme.healthcare.exception.ResourceNotFoundException;
import com.acme.healthcare.mapper.UserPatientAssignmentMapper;
import com.acme.healthcare.service.dto.UserPatientAssignmentRequest;
import com.acme.healthcare.service.dto.UserPatientAssignmentResponse;
import com.acme.healthcare.domain.enums.RoleType;
import java.time.LocalDate;
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
 * Unit tests for {@link UserPatientAssignmentServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
class UserPatientAssignmentServiceImplTest {

    @Mock
    private UserPatientAssignmentRepository assignmentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserPatientAssignmentMapper mapper;

    @InjectMocks
    private UserPatientAssignmentServiceImpl service;

    private UserPatientAssignmentRequest request;
    private User user;
    private Patient patient;
    private UserPatientAssignment assignment;
    private UserPatientAssignmentResponse response;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("doctor1");

        patient = new Patient();
        patient.setId(2L);
        patient.setMedicalRecordNumber("MRN-001");

        request = new UserPatientAssignmentRequest();
        request.setUserId(1L);
        request.setPatientId(2L);
        request.setAssignmentRole(RoleType.PHYSICIAN.name());
        request.setStartDate(LocalDate.now());

        assignment = new UserPatientAssignment();
        assignment.setId(5L);
        assignment.setUser(user);
        assignment.setPatient(patient);
        assignment.setAssignmentRole(RoleType.PHYSICIAN);

        response = new UserPatientAssignmentResponse();
        response.setId(5L);
        response.setUserId(1L);
        response.setPatientId(2L);
    }

    @Test
    void create_ShouldPersistAssignment() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(patientRepository.findById(2L)).thenReturn(Optional.of(patient));
        when(mapper.toEntity(request)).thenReturn(assignment);
        when(assignmentRepository.save(assignment)).thenReturn(assignment);
        when(mapper.toResponse(assignment)).thenReturn(response);

        UserPatientAssignmentResponse result = service.create(request);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        verify(assignmentRepository, times(1)).save(assignment);
    }

    @Test
    void create_WithMissingUser_ShouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.create(request));
        verify(assignmentRepository, never()).save(any());
    }

    @Test
    void update_ShouldMergeAndReturn() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(patientRepository.findById(2L)).thenReturn(Optional.of(patient));
        when(assignmentRepository.findById(5L)).thenReturn(Optional.of(assignment));
        when(assignmentRepository.save(assignment)).thenReturn(assignment);
        when(mapper.toResponse(assignment)).thenReturn(response);

        UserPatientAssignmentResponse result = service.update(5L, request);

        assertNotNull(result);
        verify(assignmentRepository, times(1)).save(assignment);
    }

    @Test
    void findById_ShouldReturnResponse() {
        when(assignmentRepository.findById(5L)).thenReturn(Optional.of(assignment));
        when(mapper.toResponse(assignment)).thenReturn(response);

        UserPatientAssignmentResponse result = service.findById(5L);

        assertEquals(5L, result.getId());
    }

    @Test
    void findById_WhenMissing_ShouldThrow() {
        when(assignmentRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.findById(5L));
    }

    @Test
    void findAll_ShouldReturnPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserPatientAssignment> page = new PageImpl<>(List.of(assignment), pageable, 1);

        when(assignmentRepository.findAll(pageable)).thenReturn(page);
        when(mapper.toResponse(assignment)).thenReturn(response);

        Page<UserPatientAssignmentResponse> result = service.findAll(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByUserId_ShouldReturnPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserPatientAssignment> page = new PageImpl<>(List.of(assignment), pageable, 1);

        when(assignmentRepository.findByUserId(1L, pageable)).thenReturn(page);
        when(mapper.toResponse(assignment)).thenReturn(response);

        Page<UserPatientAssignmentResponse> result = service.findByUserId(1L, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void findByPatientId_ShouldReturnPaged() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserPatientAssignment> page = new PageImpl<>(List.of(assignment), pageable, 1);

        when(assignmentRepository.findByPatientId(2L, pageable)).thenReturn(page);
        when(mapper.toResponse(assignment)).thenReturn(response);

        Page<UserPatientAssignmentResponse> result = service.findByPatientId(2L, pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void delete_WhenExists_ShouldInvokeRepository() {
        when(assignmentRepository.existsById(5L)).thenReturn(true);

        service.delete(5L);

        verify(assignmentRepository, times(1)).deleteById(5L);
    }

    @Test
    void delete_WhenMissing_ShouldThrow() {
        when(assignmentRepository.existsById(5L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.delete(5L));
        verify(assignmentRepository, never()).deleteById(anyLong());
    }
}


