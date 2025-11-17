package com.acme.healthcare.controller;

import com.acme.healthcare.service.PatientVisitService;
import com.acme.healthcare.service.dto.PatientVisitRequest;
import com.acme.healthcare.service.dto.PatientVisitResponse;
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
 * REST controller for managing patient visits.
 */
@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
public class PatientVisitController {
    
    private final PatientVisitService visitService;
    
    /**
     * Creates a new patient visit.
     *
     * @param request the visit request
     * @return the created visit
     */
    @PostMapping
    @PreAuthorize("hasAuthority('VISIT_CREATE')")
    public ResponseEntity<PatientVisitResponse> create(@Valid @RequestBody PatientVisitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(visitService.create(request));
    }
    
    /**
     * Updates an existing visit.
     *
     * @param id the visit ID
     * @param request the update request
     * @return the updated visit
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('VISIT_UPDATE')")
    public ResponseEntity<PatientVisitResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody PatientVisitRequest request) {
        return ResponseEntity.ok(visitService.update(id, request));
    }
    
    /**
     * Retrieves a visit by ID.
     *
     * @param id the visit ID
     * @return the visit
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('VISIT_READ')")
    public ResponseEntity<PatientVisitResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(visitService.findById(id));
    }
    
    /**
     * Retrieves all visits with pagination.
     *
     * @param pageable pagination parameters
     * @return page of visits
     */
    @GetMapping
    @PreAuthorize("hasAuthority('VISIT_READ')")
    public ResponseEntity<Page<PatientVisitResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(visitService.findAll(pageable));
    }
    
    /**
     * Retrieves visits by patient ID.
     *
     * @param patientId the patient ID
     * @param pageable pagination parameters
     * @return page of visits
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('VISIT_READ')")
    public ResponseEntity<Page<PatientVisitResponse>> findByPatientId(
            @PathVariable Long patientId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(visitService.findByPatientId(patientId, pageable));
    }
    
    /**
     * Deletes a visit by ID.
     *
     * @param id the visit ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('VISIT_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        visitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


