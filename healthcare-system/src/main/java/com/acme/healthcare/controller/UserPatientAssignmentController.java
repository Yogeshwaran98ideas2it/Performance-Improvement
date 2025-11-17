package com.acme.healthcare.controller;

import com.acme.healthcare.service.UserPatientAssignmentService;
import com.acme.healthcare.service.dto.UserPatientAssignmentRequest;
import com.acme.healthcare.service.dto.UserPatientAssignmentResponse;
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
 * REST controller for managing user-patient assignments.
 */
@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
public class UserPatientAssignmentController {
    
    private final UserPatientAssignmentService assignmentService;
    
    /**
     * Creates a new user-patient assignment.
     *
     * @param request the assignment request
     * @return the created assignment
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ASSIGNMENT_CREATE')")
    public ResponseEntity<UserPatientAssignmentResponse> create(@Valid @RequestBody UserPatientAssignmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(assignmentService.create(request));
    }
    
    /**
     * Updates an existing assignment.
     *
     * @param id the assignment ID
     * @param request the update request
     * @return the updated assignment
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ASSIGNMENT_UPDATE')")
    public ResponseEntity<UserPatientAssignmentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UserPatientAssignmentRequest request) {
        return ResponseEntity.ok(assignmentService.update(id, request));
    }
    
    /**
     * Retrieves an assignment by ID.
     *
     * @param id the assignment ID
     * @return the assignment
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ASSIGNMENT_READ')")
    public ResponseEntity<UserPatientAssignmentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.findById(id));
    }
    
    /**
     * Retrieves all assignments with pagination.
     *
     * @param pageable pagination parameters
     * @return page of assignments
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ASSIGNMENT_READ')")
    public ResponseEntity<Page<UserPatientAssignmentResponse>> findAll(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(assignmentService.findAll(pageable));
    }
    
    /**
     * Retrieves assignments by user ID.
     *
     * @param userId the user ID
     * @param pageable pagination parameters
     * @return page of assignments
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAuthority('ASSIGNMENT_READ')")
    public ResponseEntity<Page<UserPatientAssignmentResponse>> findByUserId(
            @PathVariable Long userId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(assignmentService.findByUserId(userId, pageable));
    }
    
    /**
     * Retrieves assignments by patient ID.
     *
     * @param patientId the patient ID
     * @param pageable pagination parameters
     * @return page of assignments
     */
    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAuthority('ASSIGNMENT_READ')")
    public ResponseEntity<Page<UserPatientAssignmentResponse>> findByPatientId(
            @PathVariable Long patientId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(assignmentService.findByPatientId(patientId, pageable));
    }
    
    /**
     * Deletes an assignment by ID.
     *
     * @param id the assignment ID
     * @return no content response
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ASSIGNMENT_DELETE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        assignmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


