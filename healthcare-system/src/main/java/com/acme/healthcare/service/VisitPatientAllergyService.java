package com.acme.healthcare.service;

import com.acme.healthcare.service.dto.VisitPatientAllergyRequest;
import com.acme.healthcare.service.dto.VisitPatientAllergyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing visit patient allergies.
 */
public interface VisitPatientAllergyService {
    
    /**
     * Creates a new allergy record.
     *
     * @param request the allergy request
     * @return the created allergy response
     */
    VisitPatientAllergyResponse create(VisitPatientAllergyRequest request);
    
    /**
     * Updates an existing allergy record.
     *
     * @param id the allergy ID
     * @param request the update request
     * @return the updated allergy response
     */
    VisitPatientAllergyResponse update(Long id, VisitPatientAllergyRequest request);
    
    /**
     * Retrieves an allergy by ID.
     *
     * @param id the allergy ID
     * @return the allergy response
     */
    VisitPatientAllergyResponse findById(Long id);
    
    /**
     * Retrieves all allergies with pagination.
     *
     * @param pageable pagination parameters
     * @return page of allergy responses
     */
    Page<VisitPatientAllergyResponse> findAll(Pageable pageable);
    
    /**
     * Retrieves allergies by visit ID.
     *
     * @param visitId the visit ID
     * @param pageable pagination parameters
     * @return page of allergy responses
     */
    Page<VisitPatientAllergyResponse> findByVisitId(Long visitId, Pageable pageable);
    
    /**
     * Deletes an allergy by ID.
     *
     * @param id the allergy ID
     */
    void delete(Long id);
}










