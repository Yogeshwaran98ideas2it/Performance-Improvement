package com.acme.health.multitenant.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link ProfileMultiTenantConfigurationStore}.
 */
class ProfileMultiTenantConfigurationStoreTest {

    private ProfileMultiTenantConfigurationStore store;
    private MultiTenantProperties properties;

    @BeforeEach
    void setUp() {
        properties = new MultiTenantProperties();
        MultiTenantProperties.StoreProperties storeConfig = new MultiTenantProperties.StoreProperties();
        
        MultiTenantProperties.DefaultStore defaults = new MultiTenantProperties.DefaultStore();
        defaults.setConnectionString("jdbc:postgresql://localhost/default");
        storeConfig.setDefaults(defaults);
        
        List<MultiTenantProperties.TenantConfiguration> tenants = new ArrayList<>();
        MultiTenantProperties.TenantConfiguration tenant1 = new MultiTenantProperties.TenantConfiguration();
        tenant1.setId("id-1");
        tenant1.setIdentifier("tenant-1");
        tenant1.setName("Tenant 1");
        tenant1.setConnectionString("jdbc:postgresql://localhost/tenant1");
        tenant1.setProperties(new HashMap<>());
        tenants.add(tenant1);
        
        storeConfig.setTenants(tenants);
        properties.setStore(storeConfig);
        
        store = new ProfileMultiTenantConfigurationStore(properties);
    }

    @Test
    void findTenant_WithValidIdentifier_ShouldReturnTenant() {
        Optional<TenantDefinition> result = store.findTenant("tenant-1");

        assertTrue(result.isPresent());
        assertEquals("tenant-1", result.get().getIdentifier());
        assertEquals("Tenant 1", result.get().getName());
    }

    @Test
    void findTenant_WithInvalidIdentifier_ShouldReturnFallback() {
        Optional<TenantDefinition> result = store.findTenant("unknown");

        assertTrue(result.isPresent());
        assertEquals("unknown", result.get().getIdentifier());
    }

    @Test
    void resolveConnection_WithValidTenant_ShouldReturnConnection() {
        Optional<String> result = store.resolveConnection("tenant-1");

        assertTrue(result.isPresent());
        assertEquals("jdbc:postgresql://localhost/tenant1", result.get());
    }

    @Test
    void resolveConnection_WithInvalidTenant_ShouldReturnDefault() {
        Optional<String> result = store.resolveConnection("unknown");

        assertTrue(result.isPresent());
        assertEquals("jdbc:postgresql://localhost/default", result.get());
    }

    @Test
    void findTenantIdentifier_WithValidKey_ShouldReturnIdentifier() {
        Optional<String> result = store.findTenantIdentifier("tenant-1");

        assertTrue(result.isPresent());
        assertEquals("tenant-1", result.get());
    }
}

