package com.acme.health.multitenant.context;

import org.springframework.util.Assert;

import java.util.Optional;

/**
 * TenantContextHolder stores the current tenant identifier in a {@link ThreadLocal}.
 */
public class TenantContextHolder {

    private final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    /**
     * Sets the current tenant identifier.
     *
     * @param tenantIdentifier the tenant identifier
     */
    public void setCurrentTenant(final String tenantIdentifier) {
        Assert.hasText(tenantIdentifier, "tenantIdentifier must not be blank");
        currentTenant.set(tenantIdentifier);
    }

    /**
     * Retrieves the current tenant identifier.
     *
     * @return an optional tenant identifier
     */
    public Optional<String> getCurrentTenant() {
        return Optional.ofNullable(currentTenant.get());
    }

    /**
     * Clears the tenant context for the current thread.
     */
    public void clear() {
        currentTenant.remove();
    }
}


