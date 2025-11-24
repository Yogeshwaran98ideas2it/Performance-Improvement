package com.acme.health.multitenant.store;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * ProfileMultiTenantConfigurationStore resolves tenants based on Spring profile configuration.
 */
public class ProfileMultiTenantConfigurationStore implements MultiTenantConfigurationStore {

    private final Map<String, TenantDefinition> tenantIndex = new HashMap<>();
    private final String defaultConnection;

    /**
     * Creates the store using the supplied properties.
     *
     * @param properties the multi-tenant properties
     */
    public ProfileMultiTenantConfigurationStore(final MultiTenantProperties properties) {
        this.defaultConnection = properties.getStore().getDefaults().getConnectionString();
        properties.getStore().getTenants().forEach(tenantConfiguration -> {
            TenantDefinition definition = new TenantDefinition();
            definition.setId(tenantConfiguration.getId());
            definition.setIdentifier(tenantConfiguration.getIdentifier());
            definition.setName(tenantConfiguration.getName());
            definition.setConnectionString(tenantConfiguration.getConnectionString());
            definition.setProperties(tenantConfiguration.getProperties());
            tenantIndex.put(definition.getIdentifier(), definition);
        });
    }

    /**
     * Finds a tenant identifier by key.
     *
     * @param tenantKey the tenant key
     * @return the tenant identifier if found
     */
    @Override
    public Optional<String> findTenantIdentifier(final String tenantKey) {
        if (tenantIndex.containsKey(tenantKey)) {
            return Optional.of(tenantKey);
        }
        return tenantIndex.values().stream()
                .filter(tenant -> tenantKey.equalsIgnoreCase(tenant.getName()))
                .findFirst()
                .map(TenantDefinition::getIdentifier);
    }

    /**
     * Resolves a tenant connection string.
     *
     * @param tenantIdentifier the tenant identifier
     * @return the connection string
     */
    @Override
    public Optional<String> resolveConnection(final String tenantIdentifier) {
        return Optional.ofNullable(tenantIndex.getOrDefault(tenantIdentifier, buildFallback(tenantIdentifier)))
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
        return Optional.ofNullable(tenantIndex.getOrDefault(tenantIdentifier, buildFallback(tenantIdentifier)));
    }

    private TenantDefinition buildFallback(final String tenantIdentifier) {
        TenantDefinition definition = new TenantDefinition();
        definition.setId("default");
        definition.setIdentifier(tenantIdentifier);
        definition.setName("Default Tenant");
        definition.setConnectionString(defaultConnection);
        return definition;
    }
}


