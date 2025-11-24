package com.acme.healthcare.messaging;

import com.acme.healthcare.common.dto.AuditEventDto;

/**
 * Publishes audit events to the external audit service.
 */
public interface AuditEventPublisher {

    /**
     * Publishes the given event.
     *
     * @param event event DTO
     */
    void publish(AuditEventDto event);
}










