package com.acme.health.multitenant.context;

import com.acme.health.multitenant.resolver.TenantResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * TenantContextFilter populates and clears the {@link TenantContextHolder} for each request.
 */
public class TenantContextFilter extends OncePerRequestFilter {

    private final TenantResolver tenantResolver;
    private final TenantContextHolder tenantContextHolder;

    /**
     * Creates a new tenant context filter.
     *
     * @param tenantResolver      the resolver chain
     * @param tenantContextHolder the context holder
     */
    public TenantContextFilter(final TenantResolver tenantResolver, final TenantContextHolder tenantContextHolder) {
        this.tenantResolver = tenantResolver;
        this.tenantContextHolder = tenantContextHolder;
    }

    /**
     * Resolves the tenant and delegates to the next filter.
     *
     * @param request  the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException when filtering fails
     * @throws IOException when filtering fails
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        try {
            tenantResolver.resolveTenant(request).ifPresent(tenantContextHolder::setCurrentTenant);
            filterChain.doFilter(request, response);
        } finally {
            tenantContextHolder.clear();
        }
    }
}


