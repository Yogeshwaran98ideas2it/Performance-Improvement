package com.acme.healthcare.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import jakarta.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;

import com.acme.health.multitenant.support.HibernateTenantIdentifierResolver;

/**
 * MultiTenantJpaConfiguration configures Hibernate for schema-based multi-tenancy.
 */
@Configuration
@Profile("!test")
@ConditionalOnProperty(prefix = "multitenant", name = "enabled", havingValue = "true", matchIfMissing = false)
@ConditionalOnBean(name = "multiTenantConnectionProvider")
@DependsOn("multiTenantConnectionProvider")
public class MultiTenantJpaConfiguration {

    private final DataSource dataSource;
    private final MultiTenantConnectionProvider connectionProvider;
    private final HibernateTenantIdentifierResolver tenantIdentifierResolver;

    /**
     * Creates the configuration with required collaborators.
     *
     * @param dataSource the base data source
     * @param connectionProvider the multi-tenant connection provider
     * @param tenantIdentifierResolver the tenant identifier resolver
     */
    @Autowired
    public MultiTenantJpaConfiguration(final DataSource dataSource,
                                       final MultiTenantConnectionProvider connectionProvider,
                                       final HibernateTenantIdentifierResolver tenantIdentifierResolver) {
        this.dataSource = dataSource;
        this.connectionProvider = connectionProvider;
        this.tenantIdentifierResolver = tenantIdentifierResolver;
    }

    /**
     * Builds the entity manager factory configured for multi-tenancy.
     *
     * @param builder the factory builder
     * @return the entity manager factory bean
     */
    @Bean(name = {"entityManagerFactory", "jpaSharedEM_entityManagerFactory"})
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(final EntityManagerFactoryBuilder builder) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.multiTenancy", "DATABASE");
        properties.put("hibernate.multi_tenant_connection_provider", connectionProvider);
        properties.put("hibernate.tenant_identifier_resolver", tenantIdentifierResolver);
        LocalContainerEntityManagerFactoryBean factory = builder
            .dataSource(dataSource)
            .packages("com.acme.healthcare.domain.entity")
            .properties(properties)
            .build();
        factory.setPersistenceUnitName("multiTenantPersistenceUnit");
        return factory;
    }

    /**
     * Configures the JPA transaction manager.
     *
     * @param entityManagerFactory the entity manager factory
     * @return the transaction manager
     */
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

