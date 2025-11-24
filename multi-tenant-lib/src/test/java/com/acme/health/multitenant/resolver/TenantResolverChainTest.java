package com.acme.health.multitenant.resolver;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TenantResolverChain}.
 */
@ExtendWith(MockitoExtension.class)
class TenantResolverChainTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HostTenantResolver hostResolver;

    @Mock
    private HeaderTenantResolver headerResolver;

    private TenantResolverChain resolverChain;

    @BeforeEach
    void setUp() {
        resolverChain = new TenantResolverChain();
        resolverChain.addResolver(hostResolver);
        resolverChain.addResolver(headerResolver);
    }

    @Test
    void resolveTenant_WithHostResolver_ShouldReturnTenant() {
        when(hostResolver.resolveTenant(request)).thenReturn(Optional.of("tenant-1"));

        Optional<String> result = resolverChain.resolveTenant(request);

        assertTrue(result.isPresent());
        assertEquals("tenant-1", result.get());
    }

    @Test
    void resolveTenant_WithHeaderResolver_ShouldReturnTenant() {
        when(hostResolver.resolveTenant(request)).thenReturn(Optional.empty());
        when(headerResolver.resolveTenant(request)).thenReturn(Optional.of("tenant-2"));

        Optional<String> result = resolverChain.resolveTenant(request);

        assertTrue(result.isPresent());
        assertEquals("tenant-2", result.get());
    }

    @Test
    void resolveTenant_WithNoResolver_ShouldReturnEmpty() {
        when(hostResolver.resolveTenant(request)).thenReturn(Optional.empty());
        when(headerResolver.resolveTenant(request)).thenReturn(Optional.empty());

        Optional<String> result = resolverChain.resolveTenant(request);

        assertTrue(result.isEmpty());
    }
}

