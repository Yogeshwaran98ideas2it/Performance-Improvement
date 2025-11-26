package com.acme.healthcare.service;

import com.acme.healthcare.service.dto.AuditResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for retrieving audit information.
 */
public interface AuditService {
    
    /**
     * Retrieves audit information for a specific entity.
     *
     * @param entityType the entity type (e.g., "Patient", "User")
     * @param entityId the entity ID
     * @return the audit response
     */
    AuditResponse getAuditInfo(String entityType, Long entityId);
    
    /**
     * Retrieves audit information for all entities of a specific type.
     *
     * @param entityType the entity type
     * @param pageable pagination parameters
     * @return page of audit responses
     */
    Page<AuditResponse> getAuditInfoByEntityType(String entityType, Pageable pageable);
    
    /**
     * Retrieves audit information filtered by user.
     *
     * @param username the username
     * @param pageable pagination parameters
     * @return page of audit responses
     */
    Page<AuditResponse> getAuditInfoByUser(String username, Pageable pageable);
}











