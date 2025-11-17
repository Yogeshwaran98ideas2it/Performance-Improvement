package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.Patient;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.entity.UserPatientAssignment;
import com.acme.healthcare.domain.enums.RoleType;
import com.acme.healthcare.domain.enums.RoleType;
import com.acme.healthcare.domain.repository.PatientRepository;
import com.acme.healthcare.domain.repository.UserPatientAssignmentRepository;
import com.acme.healthcare.domain.repository.UserRepository;
import com.acme.healthcare.exception.ResourceNotFoundException;
import com.acme.healthcare.mapper.UserPatientAssignmentMapper;
import com.acme.healthcare.service.UserPatientAssignmentService;
import com.acme.healthcare.service.dto.UserPatientAssignmentRequest;
import com.acme.healthcare.service.dto.UserPatientAssignmentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing user-patient assignments.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserPatientAssignmentServiceImpl implements UserPatientAssignmentService {
    
    private final UserPatientAssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final UserPatientAssignmentMapper mapper;
    
    @Override
    public UserPatientAssignmentResponse create(UserPatientAssignmentRequest request) {
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
        Patient patient = patientRepository.findById(request.getPatientId())
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));
        
        UserPatientAssignment assignment = mapper.toEntity(request);
        assignment.setUser(user);
        assignment.setPatient(patient);
        
        UserPatientAssignment saved = assignmentRepository.save(assignment);
        return mapper.toResponse(saved);
    }
    
    @Override
    public UserPatientAssignmentResponse update(Long id, UserPatientAssignmentRequest request) {
        UserPatientAssignment assignment = assignmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
        
        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getUserId()));
            assignment.setUser(user);
        }
        if (request.getPatientId() != null) {
            Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + request.getPatientId()));
            assignment.setPatient(patient);
        }
        if (request.getAssignmentRole() != null) {
            assignment.setAssignmentRole(RoleType.valueOf(request.getAssignmentRole().toUpperCase()));
        }
        assignment.setStartDate(request.getStartDate());
        assignment.setEndDate(request.getEndDate());
        
        UserPatientAssignment updated = assignmentRepository.save(assignment);
        return mapper.toResponse(updated);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserPatientAssignmentResponse findById(Long id) {
        UserPatientAssignment assignment = assignmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with id: " + id));
        return mapper.toResponse(assignment);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserPatientAssignmentResponse> findAll(Pageable pageable) {
        return assignmentRepository.findAll(pageable)
            .map(mapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserPatientAssignmentResponse> findByUserId(Long userId, Pageable pageable) {
        return assignmentRepository.findByUserId(userId, pageable)
            .map(mapper::toResponse);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<UserPatientAssignmentResponse> findByPatientId(Long patientId, Pageable pageable) {
        return assignmentRepository.findByPatientId(patientId, pageable)
            .map(mapper::toResponse);
    }
    
    @Override
    public void delete(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Assignment not found with id: " + id);
        }
        assignmentRepository.deleteById(id);
    }
}


