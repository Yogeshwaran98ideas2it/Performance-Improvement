package com.acme.health.multitenant.store;

import java.util.Optional;

/**
 * MultiTenantConfigurationStore exposes operations for retrieving tenant configuration details.
 */
public interface MultiTenantConfigurationStore {

    /**
     * Finds a tenant identifier by a supplied key.
     *
     * @param tenantKey the tenant key
     * @return an optional tenant identifier
     */
    Optional<String> findTenantIdentifier(String tenantKey);

    /**
     * Resolves the JDBC connection string for a tenant.
     *
     * @param tenantIdentifier the tenant identifier
     * @return an optional connection string
     */
    Optional<String> resolveConnection(String tenantIdentifier);

    /**
     * Finds the tenant definition.
     *
     * @param tenantIdentifier the tenant identifier
     * @return an optional tenant definition
     */
    Optional<TenantDefinition> findTenant(String tenantIdentifier);
}


