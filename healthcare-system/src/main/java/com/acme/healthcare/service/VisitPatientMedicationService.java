package com.acme.healthcare.service;

import com.acme.healthcare.service.dto.VisitPatientMedicationRequest;
import com.acme.healthcare.service.dto.VisitPatientMedicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing visit patient medications.
 */
public interface VisitPatientMedicationService {
    
    /**
     * Creates a new medication record.
     *
     * @param request the medication request
     * @return the created medication response
     */
    VisitPatientMedicationResponse create(VisitPatientMedicationRequest request);
    
    /**
     * Updates an existing medication record.
     *
     * @param id the medication ID
     * @param request the update request
     * @return the updated medication response
     */
    VisitPatientMedicationResponse update(Long id, VisitPatientMedicationRequest request);
    
    /**
     * Retrieves a medication by ID.
     *
     * @param id the medication ID
     * @return the medication response
     */
    VisitPatientMedicationResponse findById(Long id);
    
    /**
     * Retrieves all medications with pagination.
     *
     * @param pageable pagination parameters
     * @return page of medication responses
     */
    Page<VisitPatientMedicationResponse> findAll(Pageable pageable);
    
    /**
     * Retrieves medications by visit ID.
     *
     * @param visitId the visit ID
     * @param pageable pagination parameters
     * @return page of medication responses
     */
    Page<VisitPatientMedicationResponse> findByVisitId(Long visitId, Pageable pageable);
    
    /**
     * Deletes a medication by ID.
     *
     * @param id the medication ID
     */
    void delete(Long id);
}










