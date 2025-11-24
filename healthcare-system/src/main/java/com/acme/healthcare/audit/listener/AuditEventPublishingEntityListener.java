package com.acme.healthcare.audit.listener;

import com.acme.healthcare.audit.entity.AuditableEntity;
import com.acme.healthcare.common.dto.AuditEventDto;
import com.acme.healthcare.messaging.AuditEventPublisherBridge;
import com.acme.healthcare.config.TenantContextBridge;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * JPA entity listener that emits domain audit events to the audit service.
 */
public class AuditEventPublishingEntityListener {

    /**
     * Invoked after a new entity is persisted.
     *
     * @param entity entity instance
     */
    public void onPostPersist(final Object entity) {
        publishEvent(entity, "CREATED");
    }

    /**
     * Invoked after an entity is updated.
     *
     * @param entity entity instance
     */
    public void onPostUpdate(final Object entity) {
        publishEvent(entity, "UPDATED");
    }

    /**
     * Invoked after an entity is removed.
     *
     * @param entity entity instance
     */
    public void onPostRemove(final Object entity) {
        publishEvent(entity, "DELETED");
    }

    private void publishEvent(final Object entity, final String action) {
        if (!(entity instanceof AuditableEntity auditable)) {
            return;
        }
        Optional<Long> entityId = extractIdentifier(entity);
        if (entityId.isEmpty()) {
            return;
        }
        Map<String, Object> details = new HashMap<>();
        details.put("version", auditable.getVersion());
        AuditEventDto event = AuditEventDto.builder()
            .entityType(entity.getClass().getSimpleName())
            .entityId(entityId.get())
            .action(action)
            .username(resolveUsername())
            .timestamp(Instant.now())
            .tenantId(TenantContextBridge.currentTenant().orElse(null))
            .correlationId(resolveCorrelationId())
            .details(details)
            .build();
        AuditEventPublisherBridge.publish(event);
    }

    private Optional<Long> extractIdentifier(final Object entity) {
        try {
            Method method = entity.getClass().getMethod("getId");
            Object value = method.invoke(entity);
            if (value instanceof Long id) {
                return Optional.of(id);
            }
        } catch (ReflectiveOperationException ignored) {
            // no-op
        }
        return Optional.empty();
    }

    private String resolveUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null ? authentication.getName() : "system";
    }

    private String resolveCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }
}

