package com.acme.healthcare.audit.controller;

import com.acme.healthcare.audit.service.AuditQueryService;
import com.acme.healthcare.audit.service.AuditWriteService;
import com.acme.healthcare.common.dto.AuditEventDto;
import com.acme.healthcare.common.dto.AuditRecordResponse;
import com.acme.healthcare.common.dto.PagedResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST endpoints for querying and ingesting audit records.
 */
@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@Validated
public class AuditController {

    private final AuditQueryService queryService;
    private final AuditWriteService writeService;

    /**
     * Retrieves audits for a given entity.
     *
     * @param entityType entity type
     * @param entityId entity identifier
     * @param page page number
     * @param size page size
     * @return paged response
     */
    @GetMapping("/{entityType}/{entityId}")
    public ResponseEntity<PagedResponseDto<AuditRecordResponse>> byEntity(
        @PathVariable final String entityType,
        @PathVariable final Long entityId,
        @RequestParam(defaultValue = "0") final int page,
        @RequestParam(defaultValue = "20") final int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "occurredAt"));
        return ResponseEntity.ok(queryService.findByEntity(entityType, entityId, pageable));
    }

    /**
     * Retrieves audits by entity type.
     *
     * @param entityType entity type
     * @param page page number
     * @param size page size
     * @return paged response
     */
    @GetMapping("/entity/{entityType}")
    public ResponseEntity<PagedResponseDto<AuditRecordResponse>> byEntityType(
        @PathVariable final String entityType,
        @RequestParam(defaultValue = "0") final int page,
        @RequestParam(defaultValue = "20") final int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "occurredAt"));
        return ResponseEntity.ok(queryService.findByEntityType(entityType, pageable));
    }

    /**
     * Retrieves audits authored by the specified user.
     *
     * @param username username
     * @param page page number
     * @param size page size
     * @return paged response
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<PagedResponseDto<AuditRecordResponse>> byUser(
        @PathVariable final String username,
        @RequestParam(defaultValue = "0") final int page,
        @RequestParam(defaultValue = "20") final int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "occurredAt"));
        return ResponseEntity.ok(queryService.findByUsername(username, pageable));
    }

    /**
     * Accepts a manual audit event (used for testing or external ingestion).
     *
     * @param dto audit DTO
     * @return persisted response
     */
    @PostMapping
    public ResponseEntity<AuditRecordResponse> publish(@Valid @RequestBody final AuditEventDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(writeService.persist(dto));
    }
}


