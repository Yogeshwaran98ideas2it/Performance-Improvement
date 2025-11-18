package com.acme.healthcare.config;

import com.acme.health.multitenant.context.TenantContextHolder;
import java.util.Optional;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

/**
 * Exposes the {@link TenantContextHolder} through a static accessor for non-managed components.
 */
@Component
public class TenantContextBridge {

    private static TenantContextHolder tenantContextHolder;

    public TenantContextBridge(final ObjectProvider<TenantContextHolder> tenantContextHolder) {
        TenantContextBridge.tenantContextHolder = tenantContextHolder.getIfAvailable();
    }

    /**
     * Retrieves the current tenant identifier.
     *
     * @return optional tenant id
     */
    public static Optional<String> currentTenant() {
        return tenantContextHolder != null ? tenantContextHolder.getCurrentTenant() : Optional.empty();
    }
}

