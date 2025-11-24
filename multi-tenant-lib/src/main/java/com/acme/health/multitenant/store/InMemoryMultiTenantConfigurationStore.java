package com.acme.health.multitenant.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * InMemoryMultiTenantConfigurationStore loads tenant definitions into memory.
 */
public class InMemoryMultiTenantConfigurationStore implements MultiTenantConfigurationStore {

    private final Map<String, TenantDefinition> tenantLookup = new HashMap<>();

    /**
     * Creates the store using provided properties.
     *
     * @param properties the multi-tenant properties
     */
    public InMemoryMultiTenantConfigurationStore(final MultiTenantProperties properties) {
        properties.getStore().getTenants().forEach(tenantConfiguration -> {
            TenantDefinition definition = new TenantDefinition();
            definition.setId(tenantConfiguration.getId());
            definition.setIdentifier(tenantConfiguration.getIdentifier());
            definition.setName(tenantConfiguration.getName());
            definition.setConnectionString(tenantConfiguration.getConnectionString());
            definition.setProperties(tenantConfiguration.getProperties());
            tenantLookup.put(definition.getIdentifier(), definition);
        });
    }

    /**
     * Finds a tenant identifier.
     *
     * @param tenantKey the tenant key
     * @return the identifier
     */
    @Override
    public Optional<String> findTenantIdentifier(final String tenantKey) {
        if (tenantLookup.containsKey(tenantKey)) {
            return Optional.of(tenantKey);
        }
        return Optional.empty();
    }

    /**
     * Resolves connection string.
     *
     * @param tenantIdentifier the tenant identifier
     * @return the connection string
     */
    @Override
    public Optional<String> resolveConnection(final String tenantIdentifier) {
        return Optional.ofNullable(tenantLookup.get(tenantIdentifier))
                .map(TenantDefinition::getConnectionString);
    }

    /**
     * Finds the tenant definition.
     *
     * @param tenantIdentifier the tenant identifier
     * @return the tenant definition
     */
    @Override
    public Optional<TenantDefinition> findTenant(final String tenantIdentifier) {
        return Optional.ofNullable(tenantLookup.get(tenantIdentifier));
    }
}

