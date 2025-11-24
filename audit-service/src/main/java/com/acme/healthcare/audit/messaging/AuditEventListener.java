package com.acme.healthcare.audit.messaging;

import com.acme.healthcare.audit.service.AuditWriteService;
import com.acme.healthcare.common.dto.AuditEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumes audit events from RabbitMQ and stores them.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditEventListener {

    private final AuditWriteService writeService;

    /**
     * Processes incoming audit events.
     *
     * @param eventDto audit event payload
     */
    @RabbitListener(queues = "${messaging.audit.queue:audit-service.events}")
    public void onAuditEvent(final AuditEventDto eventDto) {
        log.debug("Received audit event {}", eventDto);
        writeService.persist(eventDto);
    }
}









