package com.acme.healthcare.service;

import com.acme.healthcare.service.dto.PatientRequest;
import com.acme.healthcare.service.dto.PatientResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * PatientService defines patient management operations.
 */
public interface PatientService {

    /**
     * Creates a patient.
     *
     * @param request the request payload
     * @return the response
     */
    PatientResponse create(PatientRequest request);

    /**
     * Updates a patient.
     *
     * @param id the identifier
     * @param request the request payload
     * @return the response
     */
    PatientResponse update(Long id, PatientRequest request);

    /**
     * Retrieves a patient.
     *
     * @param id the identifier
     * @return the response
     */
    PatientResponse get(Long id);

    /**
     * Deletes a patient.
     *
     * @param id the identifier
     */
    void delete(Long id);

    /**
     * Lists patients.
     *
     * @param pageable the pageable
     * @return the page of patients
     */
    Page<PatientResponse> list(Pageable pageable);
}










