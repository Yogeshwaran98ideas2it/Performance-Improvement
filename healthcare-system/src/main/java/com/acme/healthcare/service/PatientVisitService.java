package com.acme.healthcare.service;

import com.acme.healthcare.service.dto.PatientVisitRequest;
import com.acme.healthcare.service.dto.PatientVisitResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing patient visits.
 */
public interface PatientVisitService {
    
    /**
     * Creates a new patient visit.
     *
     * @param request the visit request
     * @return the created visit response
     */
    PatientVisitResponse create(PatientVisitRequest request);
    
    /**
     * Updates an existing visit.
     *
     * @param id the visit ID
     * @param request the update request
     * @return the updated visit response
     */
    PatientVisitResponse update(Long id, PatientVisitRequest request);
    
    /**
     * Retrieves a visit by ID.
     *
     * @param id the visit ID
     * @return the visit response
     */
    PatientVisitResponse findById(Long id);
    
    /**
     * Retrieves all visits with pagination.
     *
     * @param pageable pagination parameters
     * @return page of visit responses
     */
    Page<PatientVisitResponse> findAll(Pageable pageable);
    
    /**
     * Retrieves visits by patient ID.
     *
     * @param patientId the patient ID
     * @param pageable pagination parameters
     * @return page of visit responses
     */
    Page<PatientVisitResponse> findByPatientId(Long patientId, Pageable pageable);
    
    /**
     * Deletes a visit by ID.
     *
     * @param id the visit ID
     */
    void delete(Long id);
}











