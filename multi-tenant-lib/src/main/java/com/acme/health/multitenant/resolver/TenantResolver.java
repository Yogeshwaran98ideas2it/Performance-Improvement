package com.acme.health.multitenant.resolver;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * TenantResolver defines the contract for resolving a tenant identifier from the incoming request.
 */
public interface TenantResolver {

    /**
     * Attempts to resolve the tenant identifier from the given request.
     *
     * @param request the HTTP servlet request
     * @return an optional tenant identifier
     */
    Optional<String> resolveTenant(HttpServletRequest request);
}


