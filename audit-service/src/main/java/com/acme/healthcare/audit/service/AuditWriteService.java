package com.acme.healthcare.audit.service;

import com.acme.healthcare.audit.client.UserServiceClient;
import com.acme.healthcare.audit.domain.AuditRecord;
import com.acme.healthcare.audit.domain.AuditRecordRepository;
import com.acme.healthcare.audit.mapper.AuditMapper;
import com.acme.healthcare.common.dto.AuditEventDto;
import com.acme.healthcare.common.dto.AuditRecordResponse;
import com.acme.healthcare.common.dto.UserSummaryDto;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

/**
 * Persists audit events arriving from RabbitMQ or REST gateways.
 */
@Service
@RequiredArgsConstructor
public class AuditWriteService {

    private final AuditRecordRepository repository;
    private final AuditMapper mapper;
    private final ObjectProvider<UserServiceClient> userServiceClient;

    /**
     * Stores the supplied audit event.
     *
     * @param dto event DTO
     * @return persisted record
     */
    @Transactional
    public AuditRecordResponse persist(final AuditEventDto dto) {
        AuditRecord record = repository.save(AuditRecord.fromDto(dto));
        userServiceClient.ifAvailable(client -> enrich(record, client));
        return mapper.toResponse(record);
    }

    private void enrich(final AuditRecord record, final UserServiceClient client) {
        try {
            Optional<UserSummaryDto> summary = Optional.ofNullable(client.getUserById(record.getEntityId()));
            summary.ifPresent(user -> record.getAttributes().put("userEmail", user.getEmail()));
        } catch (Exception ignored) {
            // Downstream service may be unavailable; enrichment is best-effort.
        }
    }
}

