package com.acme.healthcare.controller;

import com.acme.healthcare.service.AuditService;
import com.acme.healthcare.service.dto.AuditResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for retrieving audit information.
 */
@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {
    
    private final AuditService auditService;
    
    /**
     * Retrieves audit information for a specific entity.
     *
     * @param entityType the entity type
     * @param entityId the entity ID
     * @return the audit information
     */
    @GetMapping("/{entityType}/{entityId}")
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<AuditResponse> getAuditInfo(
            @PathVariable String entityType,
            @PathVariable Long entityId) {
        return ResponseEntity.ok(auditService.getAuditInfo(entityType, entityId));
    }
    
    /**
     * Retrieves audit information for all entities of a specific type.
     *
     * @param entityType the entity type
     * @param pageable pagination parameters
     * @return page of audit information
     */
    @GetMapping("/entity/{entityType}")
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<Page<AuditResponse>> getAuditInfoByEntityType(
            @PathVariable String entityType,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(auditService.getAuditInfoByEntityType(entityType, pageable));
    }
    
    /**
     * Retrieves audit information filtered by user.
     *
     * @param username the username
     * @param pageable pagination parameters
     * @return page of audit information
     */
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<Page<AuditResponse>> getAuditInfoByUser(
            @PathVariable String username,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(auditService.getAuditInfoByUser(username, pageable));
    }
}


