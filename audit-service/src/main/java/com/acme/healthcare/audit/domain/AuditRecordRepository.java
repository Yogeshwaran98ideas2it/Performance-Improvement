package com.acme.healthcare.audit.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for audit records.
 */
public interface AuditRecordRepository extends JpaRepository<AuditRecord, Long> {

    /**
     * Finds audit records for a specific entity.
     *
     * @param entityType entity type
     * @param entityId entity identifier
     * @param pageable paging metadata
     * @return page of audit records
     */
    Page<AuditRecord> findByEntityTypeIgnoreCaseAndEntityId(String entityType, Long entityId, Pageable pageable);

    /**
     * Retrieves audit records authored by a specific user.
     *
     * @param username username
     * @param pageable paging metadata
     * @return page of audit records
     */
    Page<AuditRecord> findByUsernameIgnoreCase(String username, Pageable pageable);

    /**
     * Retrieves audit records for an entity type irrespective of identifier.
     *
     * @param entityType entity type
     * @param pageable paging metadata
     * @return page of audit records
     */
    Page<AuditRecord> findByEntityTypeIgnoreCase(String entityType, Pageable pageable);
}


