package com.acme.health.multitenant.support;

import com.acme.health.multitenant.store.MultiTenantConfigurationStore;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MultiTenantConnectionProviderImpl supplies tenant-specific {@link DataSource} instances to Hibernate.
 */
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

    private final MultiTenantConfigurationStore configurationStore;
    private final DataSource baseDataSource;
    private final Map<String, DataSource> cache = new ConcurrentHashMap<>();
    private String defaultUsername;
    private String defaultPassword;

    /**
     * Creates the provider with the configuration store and base data source.
     *
     * @param configurationStore the configuration store
     * @param baseDataSource the base data source to extract default credentials
     */
    public MultiTenantConnectionProviderImpl(final MultiTenantConfigurationStore configurationStore,
                                             final DataSource baseDataSource) {
        this.configurationStore = configurationStore;
        this.baseDataSource = baseDataSource;
        extractCredentials();
    }

    /**
     * Extracts default credentials from the base data source.
     */
    private void extractCredentials() {
        try {
            if (baseDataSource instanceof HikariDataSource) {
                HikariDataSource hikariDataSource = (HikariDataSource) baseDataSource;
                defaultUsername = hikariDataSource.getUsername();
                defaultPassword = hikariDataSource.getPassword();
            } else {
                // Try to get credentials from connection
                try (Connection conn = baseDataSource.getConnection()) {
                    // Use system properties or environment variables as fallback
                    defaultUsername = System.getProperty("spring.datasource.username",
                            System.getenv("SPRING_DATASOURCE_USERNAME"));
                    defaultPassword = System.getProperty("spring.datasource.password",
                            System.getenv("SPRING_DATASOURCE_PASSWORD"));
                }
            }
        } catch (SQLException e) {
            // Fallback to environment variables
            defaultUsername = System.getProperty("spring.datasource.username",
                    System.getenv("SPRING_DATASOURCE_USERNAME"));
            defaultPassword = System.getProperty("spring.datasource.password",
                    System.getenv("SPRING_DATASOURCE_PASSWORD"));
        }
        // Final fallback
        if (defaultUsername == null) {
            defaultUsername = "healthcare_app";
        }
        if (defaultPassword == null) {
            defaultPassword = "change-me";
        }
    }

    /**
     * Gets the default data source.
     *
     * @return the default data source
     */
    @Override
    protected DataSource selectAnyDataSource() {
        return cache.computeIfAbsent("default", this::buildDataSource);
    }

    /**
     * Gets the specific tenant data source.
     *
     * @param tenantIdentifier the tenant identifier
     * @return the tenant data source
     */
    @Override
    protected DataSource selectDataSource(final Object tenantIdentifier) {
        String tenantId = tenantIdentifier != null ? tenantIdentifier.toString() : "default";
        return cache.computeIfAbsent(tenantId, this::buildDataSource);
    }

    private DataSource buildDataSource(final String tenantIdentifier) {
        HikariConfig config = new HikariConfig();
        String jdbcUrl = configurationStore.resolveConnection(tenantIdentifier).orElse(
                configurationStore.resolveConnection("default").orElse("jdbc:h2:mem:" + tenantIdentifier)
        );
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(defaultUsername);
        config.setPassword(defaultPassword);
        config.setPoolName("tenant-" + tenantIdentifier);
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setRegisterMbeans(true);
        return new HikariDataSource(config);
    }
}

