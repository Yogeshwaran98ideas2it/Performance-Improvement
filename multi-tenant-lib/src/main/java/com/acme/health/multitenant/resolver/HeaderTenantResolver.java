package com.acme.health.multitenant.resolver;

import com.acme.health.multitenant.store.MultiTenantConfigurationStore;
import com.acme.health.multitenant.store.MultiTenantProperties;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * HeaderTenantResolver resolves tenant identifiers from a configured HTTP header.
 */
public class HeaderTenantResolver implements TenantResolver {

    private final MultiTenantConfigurationStore configurationStore;
    private final MultiTenantProperties properties;

    /**
     * Creates a new header-based resolver.
     *
     * @param configurationStore the configuration store
     * @param properties         the multi-tenant properties
     */
    public HeaderTenantResolver(final MultiTenantConfigurationStore configurationStore,
                                final MultiTenantProperties properties) {
        this.configurationStore = configurationStore;
        this.properties = properties;
    }

    /**
     * Resolves the tenant identifier from the configured header.
     *
     * @param request the HTTP request
     * @return an optional tenant identifier
     */
    @Override
    public Optional<String> resolveTenant(final HttpServletRequest request) {
        if (!properties.getResolution().isHeaderEnabled()) {
            return Optional.empty();
        }
        String headerName = properties.getResolution().getHeaderName();
        String headerValue = request.getHeader(headerName);
        if (headerValue == null) {
            return Optional.empty();
        }
        return configurationStore.findTenantIdentifier(headerValue.trim());
    }
}


