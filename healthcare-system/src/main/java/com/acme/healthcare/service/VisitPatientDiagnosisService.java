package com.acme.healthcare.service;

import com.acme.healthcare.service.dto.VisitPatientDiagnosisRequest;
import com.acme.healthcare.service.dto.VisitPatientDiagnosisResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing visit patient diagnoses.
 */
public interface VisitPatientDiagnosisService {
    
    /**
     * Creates a new diagnosis.
     *
     * @param request the diagnosis request
     * @return the created diagnosis response
     */
    VisitPatientDiagnosisResponse create(VisitPatientDiagnosisRequest request);
    
    /**
     * Updates an existing diagnosis.
     *
     * @param id the diagnosis ID
     * @param request the update request
     * @return the updated diagnosis response
     */
    VisitPatientDiagnosisResponse update(Long id, VisitPatientDiagnosisRequest request);
    
    /**
     * Retrieves a diagnosis by ID.
     *
     * @param id the diagnosis ID
     * @return the diagnosis response
     */
    VisitPatientDiagnosisResponse findById(Long id);
    
    /**
     * Retrieves all diagnoses with pagination.
     *
     * @param pageable pagination parameters
     * @return page of diagnosis responses
     */
    Page<VisitPatientDiagnosisResponse> findAll(Pageable pageable);
    
    /**
     * Retrieves diagnoses by visit ID.
     *
     * @param visitId the visit ID
     * @param pageable pagination parameters
     * @return page of diagnosis responses
     */
    Page<VisitPatientDiagnosisResponse> findByVisitId(Long visitId, Pageable pageable);
    
    /**
     * Deletes a diagnosis by ID.
     *
     * @param id the diagnosis ID
     */
    void delete(Long id);
}











