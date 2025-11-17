package com.acme.healthcare.controller;

import com.acme.healthcare.service.PatientService;
import com.acme.healthcare.service.dto.PatientRequest;
import com.acme.healthcare.service.dto.PatientResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PatientController exposes patient management endpoints.
 */
@RestController
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientService patientService;

    /**
     * Creates the controller.
     *
     * @param patientService the patient service
     */
    public PatientController(final PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Creates a patient record.
     *
     * @param request the request payload
     * @return the created patient
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PATIENT_CREATE')")
    public ResponseEntity<PatientResponse> create(@Valid @RequestBody final PatientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.create(request));
    }

    /**
     * Updates a patient record.
     *
     * @param id the identifier
     * @param request the request payload
     * @return the updated patient
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT_UPDATE')")
    public ResponseEntity<PatientResponse> update(@PathVariable final Long id,
                                                  @Valid @RequestBody final PatientRequest request) {
        return ResponseEntity.ok(patientService.update(id, request));
    }

    /**
     * Retrieves a patient record.
     *
     * @param id the identifier
     * @return the patient
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    public ResponseEntity<PatientResponse> get(@PathVariable final Long id) {
        return ResponseEntity.ok(patientService.get(id));
    }

    /**
     * Lists patients with pagination.
     *
     * @param pageable the pageable
     * @return the paged patients
     */
    @GetMapping
    @PreAuthorize("hasAuthority('PATIENT_READ')")
    public ResponseEntity<Page<PatientResponse>> list(final Pageable pageable) {
        return ResponseEntity.ok(patientService.list(pageable));
    }

    /**
     * Deletes a patient record.
     *
     * @param id the identifier
     * @return the response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PATIENT_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable final Long id) {
        patientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


