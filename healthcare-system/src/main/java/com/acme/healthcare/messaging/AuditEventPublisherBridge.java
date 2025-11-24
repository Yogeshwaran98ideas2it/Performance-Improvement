package com.acme.healthcare.messaging;

import com.acme.healthcare.common.dto.AuditEventDto;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.ObjectProvider;

/**
 * Static bridge that exposes the {@link AuditEventPublisher} to JPA entity listeners.
 */
@Component
public class AuditEventPublisherBridge {

    private static AuditEventPublisher delegate;

    public AuditEventPublisherBridge(final ObjectProvider<AuditEventPublisher> publisher) {
        AuditEventPublisherBridge.delegate = publisher.getIfAvailable();
    }

    /**
     * Publishes the event if a delegate is available.
     *
     * @param event audit event
     */
    public static void publish(final AuditEventDto event) {
        if (delegate != null) {
            delegate.publish(event);
        }
    }
}

