package com.acme.healthcare.service;

import com.acme.healthcare.service.dto.UserPatientAssignmentRequest;
import com.acme.healthcare.service.dto.UserPatientAssignmentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing user-patient assignments.
 */
public interface UserPatientAssignmentService {
    
    /**
     * Creates a new user-patient assignment.
     *
     * @param request the assignment request
     * @return the created assignment response
     */
    UserPatientAssignmentResponse create(UserPatientAssignmentRequest request);
    
    /**
     * Updates an existing assignment.
     *
     * @param id the assignment ID
     * @param request the update request
     * @return the updated assignment response
     */
    UserPatientAssignmentResponse update(Long id, UserPatientAssignmentRequest request);
    
    /**
     * Retrieves an assignment by ID.
     *
     * @param id the assignment ID
     * @return the assignment response
     */
    UserPatientAssignmentResponse findById(Long id);
    
    /**
     * Retrieves all assignments with pagination.
     *
     * @param pageable pagination parameters
     * @return page of assignment responses
     */
    Page<UserPatientAssignmentResponse> findAll(Pageable pageable);
    
    /**
     * Retrieves assignments by user ID.
     *
     * @param userId the user ID
     * @param pageable pagination parameters
     * @return page of assignment responses
     */
    Page<UserPatientAssignmentResponse> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Retrieves assignments by patient ID.
     *
     * @param patientId the patient ID
     * @param pageable pagination parameters
     * @return page of assignment responses
     */
    Page<UserPatientAssignmentResponse> findByPatientId(Long patientId, Pageable pageable);
    
    /**
     * Deletes an assignment by ID.
     *
     * @param id the assignment ID
     */
    void delete(Long id);
}










