package com.acme.healthcare.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * DefaultJpaConfiguration provides standard JPA configuration when multi-tenancy is disabled.
 */
@Configuration
@ConditionalOnProperty(prefix = "multitenant", name = "enabled", havingValue = "false", matchIfMissing = true)
@EnableJpaRepositories(
    basePackages = "com.acme.healthcare.domain.repository",
    entityManagerFactoryRef = "jpaSharedEM_entityManagerFactory",
    transactionManagerRef = "transactionManager"
)
public class DefaultJpaConfiguration {

    /**
     * Creates the entity manager factory bean with the name Spring Data JPA expects.
     *
     * @param builder the factory builder
     * @param dataSource the data source
     * @return the entity manager factory bean
     */
    @Bean(name = {"entityManagerFactory", "jpaSharedEM_entityManagerFactory"})
    @Primary
    @ConditionalOnMissingBean(name = "jpaSharedEM_entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            final EntityManagerFactoryBuilder builder,
            final DataSource dataSource) {
        return builder
            .dataSource(dataSource)
            .packages("com.acme.healthcare.domain.entity")
            .build();
    }

    /**
     * Configures the JPA transaction manager.
     *
     * @param entityManagerFactory the entity manager factory
     * @return the transaction manager
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

