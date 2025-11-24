package com.acme.health.multitenant.resolver;

import com.acme.health.multitenant.store.MultiTenantConfigurationStore;
import com.acme.health.multitenant.store.MultiTenantProperties;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * HostTenantResolver resolves the tenant identifier from the request host by using the subdomain.
 */
public class HostTenantResolver implements TenantResolver {

    private final MultiTenantConfigurationStore configurationStore;
    private final MultiTenantProperties properties;

    /**
     * Creates a new host-based resolver.
     *
     * @param configurationStore the configuration store
     * @param properties         the multi-tenant properties
     */
    public HostTenantResolver(final MultiTenantConfigurationStore configurationStore,
                              final MultiTenantProperties properties) {
        this.configurationStore = configurationStore;
        this.properties = properties;
    }

    /**
     * Resolves the tenant identifier from the request host.
     *
     * @param request the HTTP request
     * @return the resolved tenant identifier
     */
    @Override
    public Optional<String> resolveTenant(final HttpServletRequest request) {
        if (!properties.getResolution().isHostEnabled()) {
            return Optional.empty();
        }
        String host = request.getServerName();
        if (host == null || host.isBlank()) {
            return Optional.empty();
        }
        try {
            URI uri = new URI(request.getScheme(), host, null, null);
            String computedHost = uri.getHost();
            if (computedHost == null) {
                return Optional.empty();
            }
            String[] segments = computedHost.split("\\.");
            if (segments.length < 3) {
                return Optional.empty();
            }
            String candidate = segments[0];
            return configurationStore.findTenantIdentifier(candidate);
        } catch (URISyntaxException e) {
            return Optional.empty();
        }
    }
}


