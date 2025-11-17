package com.acme.healthcare.controller;

import com.acme.healthcare.service.VisitPatientAllergyService;
import com.acme.healthcare.service.dto.VisitPatientAllergyRequest;
import com.acme.healthcare.service.dto.VisitPatientAllergyResponse;
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
 * REST controller for managing visit patient allergies.
 */
@RestController
@RequestMapping("/api/v1/allergies")
@RequiredArgsConstructor
public class VisitPatientAllergyController {
    
    private final VisitPatientAllergyService allergyService;
    
    /**
     * Creates a new allergy record.
     *
     * @param request the allergy request
     * @return the created allergy
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ALLERGY_CREATE')")
    public ResponseEntity<VisitPatientAllergyResponse> create(@Valid @RequestBody VisitPatientAllergyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(allergyService.create(request));
    }
    
    /**
     * Updates an existing allergy record.
     *
     * @param id the allergy ID
     * @param request the update request
     * @return the updated allergy
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ALLERGY_UPDATE')")
    public ResponseEntity<VisitPatientAllergyResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody VisitPatientAllergyRequest request) {
        return ResponseEntity.ok(allergyService.update(id, request));
    }
    
    /**
     * Retrieves an allergy by ID.
     *
     * @param id the allergy ID
     * @return the allergy
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ALLERGY_READ')")
    public ResponseEntity<VisitPatientAllergyResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(allergyService.findById(id));
    }
    
    /**
     * Retrieves all allergies with pagination.
     *
     * @param pageable pagination parameters
     * @return page of allergies
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ALLERGY_READ')")
    public ResponseEntity<Page<VisitPatientAllergyResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(allergyService.findAll(pageable));
    }
    
    /**
     * Retrieves allergies by visit ID.
     *
     * @param visitId the visit ID
     * @param pageable pagination parameters
     * @return page of allergies
     */
    @GetMapping("/visit/{visitId}")
    @PreAuthorize("hasAuthority('ALLERGY_READ')")
    public ResponseEntity<Page<VisitPatientAllergyResponse>> findByVisitId(
            @PathVariable Long visitId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(allergyService.findByVisitId(visitId, pageable));
    }
    
    /**
     * Deletes an allergy by ID.
     *
     * @param id the allergy ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ALLERGY_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        allergyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


