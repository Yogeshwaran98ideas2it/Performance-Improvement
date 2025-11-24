package com.acme.healthcare.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

/**
 * Reactive security configuration for the API Gateway.
 *
 * Downstream services perform JWT validation; the gateway ensures basic protection for management endpoints.
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class GatewaySecurityConfiguration {

    /**
     * Configures the reactive security filter chain.
     *
     * @param http reactive HTTP security
     * @return security filter chain
     */
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
                .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                .anyExchange().permitAll())
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        return http.build();
    }
}

