package com.acme.health.multitenant.resolver;

import jakarta.servlet.http.HttpServletRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * TenantResolverChain resolves tenants by delegating to a list of resolver strategies.
 */
public class TenantResolverChain implements TenantResolver {

    private final List<TenantResolver> delegates = new LinkedList<>();

    /**
     * Adds a resolver to the chain.
     *
     * @param resolver the resolver to add
     */
    public void addResolver(final TenantResolver resolver) {
        delegates.add(resolver);
    }

    /**
     * Resolves the tenant identifier using the registered delegates.
     *
     * @param request the HTTP servlet request
     * @return an optional tenant identifier
     */
    @Override
    public Optional<String> resolveTenant(final HttpServletRequest request) {
        return delegates.stream()
                .map(resolver -> resolver.resolveTenant(request))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}

