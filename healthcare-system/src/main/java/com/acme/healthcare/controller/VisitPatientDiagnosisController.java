package com.acme.healthcare.controller;

import com.acme.healthcare.service.VisitPatientDiagnosisService;
import com.acme.healthcare.service.dto.VisitPatientDiagnosisRequest;
import com.acme.healthcare.service.dto.VisitPatientDiagnosisResponse;
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
 * REST controller for managing visit patient diagnoses.
 */
@RestController
@RequestMapping("/api/v1/diagnoses")
@RequiredArgsConstructor
public class VisitPatientDiagnosisController {
    
    private final VisitPatientDiagnosisService diagnosisService;
    
    /**
     * Creates a new diagnosis.
     *
     * @param request the diagnosis request
     * @return the created diagnosis
     */
    @PostMapping
    @PreAuthorize("hasAuthority('DIAGNOSIS_CREATE')")
    public ResponseEntity<VisitPatientDiagnosisResponse> create(@Valid @RequestBody VisitPatientDiagnosisRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(diagnosisService.create(request));
    }
    
    /**
     * Updates an existing diagnosis.
     *
     * @param id the diagnosis ID
     * @param request the update request
     * @return the updated diagnosis
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('DIAGNOSIS_UPDATE')")
    public ResponseEntity<VisitPatientDiagnosisResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VisitPatientDiagnosisRequest request) {
        return ResponseEntity.ok(diagnosisService.update(id, request));
    }
    
    /**
     * Retrieves a diagnosis by ID.
     *
     * @param id the diagnosis ID
     * @return the diagnosis
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('DIAGNOSIS_READ')")
    public ResponseEntity<VisitPatientDiagnosisResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(diagnosisService.findById(id));
    }
    
    /**
     * Retrieves all diagnoses with pagination.
     *
     * @param pageable pagination parameters
     * @return page of diagnoses
     */
    @GetMapping
    @PreAuthorize("hasAuthority('DIAGNOSIS_READ')")
    public ResponseEntity<Page<VisitPatientDiagnosisResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(diagnosisService.findAll(pageable));
    }
    
    /**
     * Retrieves diagnoses by visit ID.
     *
     * @param visitId the visit ID
     * @param pageable pagination parameters
     * @return page of diagnoses
     */
    @GetMapping("/visit/{visitId}")
    @PreAuthorize("hasAuthority('DIAGNOSIS_READ')")
    public ResponseEntity<Page<VisitPatientDiagnosisResponse>> findByVisitId(
            @PathVariable Long visitId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(diagnosisService.findByVisitId(visitId, pageable));
    }
    
    /**
     * Deletes a diagnosis by ID.
     *
     * @param id the diagnosis ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DIAGNOSIS_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        diagnosisService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


