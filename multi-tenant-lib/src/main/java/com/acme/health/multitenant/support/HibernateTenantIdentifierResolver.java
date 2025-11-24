package com.acme.health.multitenant.support;

import com.acme.health.multitenant.context.TenantContextHolder;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

import java.util.Optional;

/**
 * HibernateTenantIdentifierResolver bridges the tenant context to Hibernate.
 */
public class HibernateTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    private final TenantContextHolder tenantContextHolder;

    /**
     * Creates the resolver using the context holder.
     *
     * @param tenantContextHolder the tenant context holder
     */
    public HibernateTenantIdentifierResolver(final TenantContextHolder tenantContextHolder) {
        this.tenantContextHolder = tenantContextHolder;
    }

    /**
     * Retrieves the current tenant identifier or default.
     *
     * @return the tenant identifier
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
        Optional<String> tenant = tenantContextHolder.getCurrentTenant();
        return tenant.orElse("default");
    }

    /**
     * Indicates if validation is required.
     *
     * @return true to validate existing sessions
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}


