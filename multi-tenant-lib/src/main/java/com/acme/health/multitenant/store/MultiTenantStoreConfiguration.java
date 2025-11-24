package com.acme.health.multitenant.store;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MultiTenantStoreConfiguration exposes configuration store beans.
 */
@Configuration
@ConditionalOnProperty(prefix = "multitenant", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MultiTenantStoreConfiguration {

    /**
     * Configures the multi-tenant configuration store based on properties.
     *
     * @param properties the properties
     * @return the configuration store
     */
    @Bean
    @ConditionalOnMissingBean
    public MultiTenantConfigurationStore multiTenantConfigurationStore(final MultiTenantProperties properties) {
        if (properties.getStore().getType() == MultiTenantProperties.StoreType.IN_MEMORY) {
            return new InMemoryMultiTenantConfigurationStore(properties);
        }
        return new ProfileMultiTenantConfigurationStore(properties);
    }
}


