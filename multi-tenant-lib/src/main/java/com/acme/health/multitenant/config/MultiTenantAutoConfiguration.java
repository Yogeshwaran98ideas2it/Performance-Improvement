package com.acme.health.multitenant.config;

import com.acme.health.multitenant.context.TenantContextFilter;
import com.acme.health.multitenant.context.TenantContextHolder;
import com.acme.health.multitenant.resolver.HeaderTenantResolver;
import com.acme.health.multitenant.resolver.HostTenantResolver;
import com.acme.health.multitenant.resolver.TenantResolver;
import com.acme.health.multitenant.resolver.TenantResolverChain;
import com.acme.health.multitenant.store.MultiTenantConfigurationStore;
import com.acme.health.multitenant.store.MultiTenantProperties;
import com.acme.health.multitenant.support.HibernateTenantIdentifierResolver;
import com.acme.health.multitenant.support.MultiTenantConnectionProviderImpl;
import jakarta.servlet.Filter;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.ServiceContributor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

/**
 * MultiTenantAutoConfiguration auto-configures the shared multi-tenant infrastructure.
 */
@AutoConfiguration
@EnableConfigurationProperties(MultiTenantProperties.class)
@ConditionalOnProperty(prefix = "multitenant", name = "enabled", havingValue = "true", matchIfMissing = true)
public class MultiTenantAutoConfiguration {

    /**
     * Creates the {@link TenantContextHolder} bean to manage tenant context storage.
     *
     * @return a configured {@link TenantContextHolder}
     */
    @Bean
    @ConditionalOnMissingBean
    public TenantContextHolder tenantContextHolder() {
        return new TenantContextHolder();
    }

    /**
     * Creates the default {@link TenantResolverChain} with host and header strategies.
     *
     * @param properties         the multi-tenant properties
     * @param configurationStore the configuration store
     * @return the {@link TenantResolverChain}
     */
    @Bean
    @ConditionalOnMissingBean
    public TenantResolverChain tenantResolverChain(final MultiTenantProperties properties,
                                                   final MultiTenantConfigurationStore configurationStore) {
        TenantResolverChain chain = new TenantResolverChain();
        chain.addResolver(new HostTenantResolver(configurationStore, properties));
        chain.addResolver(new HeaderTenantResolver(configurationStore, properties));
        return chain;
    }

    /**
     * Registers the {@link TenantContextFilter}.
     *
     * @param tenantContextHolder the tenant context holder
     * @param tenantResolver      the tenant resolver
     * @return the filter registration bean
     */
    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean<Filter> tenantContextFilterRegistration(final TenantContextHolder tenantContextHolder,
                                                                          final TenantResolver tenantResolver) {
        TenantContextFilter filter = new TenantContextFilter(tenantResolver, tenantContextHolder);
        FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

    /**
     * Creates the {@link HibernateTenantIdentifierResolver}.
     *
     * @param tenantContextHolder the tenant context holder
     * @return the identifier resolver
     */
    @Bean
    @ConditionalOnMissingBean
    public HibernateTenantIdentifierResolver hibernateTenantIdentifierResolver(final TenantContextHolder tenantContextHolder) {
        return new HibernateTenantIdentifierResolver(tenantContextHolder);
    }

    /**
     * Creates the {@link MultiTenantConnectionProvider}.
     *
     * @param configurationStore the tenant configuration store
     * @param dataSource the base data source to extract default credentials
     * @return the connection provider
     */
    @Bean
    @ConditionalOnMissingBean
    public MultiTenantConnectionProvider multiTenantConnectionProvider(final MultiTenantConfigurationStore configurationStore,
                                                                      final javax.sql.DataSource dataSource) {
        return new MultiTenantConnectionProviderImpl(configurationStore, dataSource);
    }

    /**
     * Provides a no-op {@link ServiceContributor} to register multi-tenant services.
     *
     * @return the service contributor
     */
    @Bean
    @ConditionalOnMissingBean
    public ServiceContributor multiTenantServiceContributor() {
        return (serviceRegistryBuilder) -> {
            // No-op contributor reserved for future extension
        };
    }
}


