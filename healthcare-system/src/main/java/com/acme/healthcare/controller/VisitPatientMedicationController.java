package com.acme.healthcare.controller;

import com.acme.healthcare.service.VisitPatientMedicationService;
import com.acme.healthcare.service.dto.VisitPatientMedicationRequest;
import com.acme.healthcare.service.dto.VisitPatientMedicationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing visit patient medications.
 */
@RestController
@RequestMapping("/api/v1/medications")
@RequiredArgsConstructor
public class VisitPatientMedicationController {
    
    private final VisitPatientMedicationService medicationService;
    
    /**
     * Creates a new medication record.
     *
     * @param request the medication request
     * @return the created medication
     */
    @PostMapping
    @PreAuthorize("hasAuthority('MEDICATION_CREATE')")
    public ResponseEntity<VisitPatientMedicationResponse> create(@Valid @RequestBody VisitPatientMedicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(medicationService.create(request));
    }
    
    /**
     * Updates an existing medication record.
     *
     * @param id the medication ID
     * @param request the update request
     * @return the updated medication
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MEDICATION_UPDATE')")
    public ResponseEntity<VisitPatientMedicationResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VisitPatientMedicationRequest request) {
        return ResponseEntity.ok(medicationService.update(id, request));
    }
    
    /**
     * Retrieves a medication by ID.
     *
     * @param id the medication ID
     * @return the medication
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MEDICATION_READ')")
    public ResponseEntity<VisitPatientMedicationResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(medicationService.findById(id));
    }
    
    /**
     * Retrieves all medications with pagination.
     *
     * @param pageable pagination parameters
     * @return page of medications
     */
    @GetMapping
    @PreAuthorize("hasAuthority('MEDICATION_READ')")
    public ResponseEntity<Page<VisitPatientMedicationResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(medicationService.findAll(pageable));
    }
    
    /**
     * Retrieves medications by visit ID.
     *
     * @param visitId the visit ID
     * @param pageable pagination parameters
     * @return page of medications
     */
    @GetMapping("/visit/{visitId}")
    @PreAuthorize("hasAuthority('MEDICATION_READ')")
    public ResponseEntity<Page<VisitPatientMedicationResponse>> findByVisitId(
            @PathVariable Long visitId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(medicationService.findByVisitId(visitId, pageable));
    }
    
    /**
     * Deletes a medication by ID.
     *
     * @param id the medication ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MEDICATION_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        medicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


