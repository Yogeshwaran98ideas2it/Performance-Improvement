package com.acme.healthcare.messaging;

import com.acme.healthcare.common.dto.AuditEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ implementation of {@link AuditEventPublisher}.
 */
@Component
@ConditionalOnBean(RabbitTemplate.class)
@RequiredArgsConstructor
@Slf4j
public class RabbitAuditEventPublisher implements AuditEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${messaging.audit.exchange:audit.events}")
    private String auditExchange;

    @Override
    public void publish(final AuditEventDto event) {
        try {
            rabbitTemplate.convertAndSend(auditExchange, "", event);
        } catch (Exception ex) {
            log.warn("Failed to publish audit event {}", event, ex);
        }
    }
}










