package com.acme.healthcare.messaging;

import com.acme.healthcare.common.dto.AuditEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * Fallback publisher used when RabbitMQ is not available (e.g., local tests).
 */
@Component
@ConditionalOnMissingBean(AuditEventPublisher.class)
@Slf4j
public class NoOpAuditEventPublisher implements AuditEventPublisher {

    @Override
    public void publish(final AuditEventDto event) {
        log.debug("Audit event publishing skipped (no transport configured): {}", event);
    }
}










