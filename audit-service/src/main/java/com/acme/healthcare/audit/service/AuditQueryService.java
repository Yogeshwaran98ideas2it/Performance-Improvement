package com.acme.healthcare.audit.service;

import com.acme.healthcare.audit.domain.AuditRecordRepository;
import com.acme.healthcare.audit.mapper.AuditMapper;
import com.acme.healthcare.common.dto.AuditRecordResponse;
import com.acme.healthcare.common.dto.PagedResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Provides read-only access to stored audit records.
 */
@Service
@RequiredArgsConstructor
public class AuditQueryService {

    private final AuditRecordRepository repository;
    private final AuditMapper mapper;

    /**
     * Retrieves records for a specific entity.
     *
     * @param entityType entity type
     * @param entityId entity identifier
     * @param pageable paging metadata
     * @return page of audit records
     */
    public PagedResponseDto<AuditRecordResponse> findByEntity(final String entityType,
                                                              final Long entityId,
                                                              final Pageable pageable) {
        Page<AuditRecordResponse> page = repository
            .findByEntityTypeIgnoreCaseAndEntityId(entityType, entityId, pageable)
            .map(mapper::toResponse);
        return toPagedResponse(page);
    }

    /**
     * Retrieves records authored by a user.
     *
     * @param username username
     * @param pageable paging metadata
     * @return page of audit records
     */
    public PagedResponseDto<AuditRecordResponse> findByUsername(final String username, final Pageable pageable) {
        Page<AuditRecordResponse> page = repository
            .findByUsernameIgnoreCase(username, pageable)
            .map(mapper::toResponse);
        return toPagedResponse(page);
    }

    /**
     * Retrieves records for a specific entity type.
     *
     * @param entityType entity type
     * @param pageable paging metadata
     * @return page of audit records
     */
    public PagedResponseDto<AuditRecordResponse> findByEntityType(final String entityType, final Pageable pageable) {
        Page<AuditRecordResponse> page = repository
            .findByEntityTypeIgnoreCase(entityType, pageable)
            .map(mapper::toResponse);
        return toPagedResponse(page);
    }

    private PagedResponseDto<AuditRecordResponse> toPagedResponse(final Page<AuditRecordResponse> page) {
        return PagedResponseDto.<AuditRecordResponse>builder()
            .content(page.getContent())
            .pageNumber(page.getNumber())
            .pageSize(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .first(page.isFirst())
            .last(page.isLast())
            .build();
    }
}


