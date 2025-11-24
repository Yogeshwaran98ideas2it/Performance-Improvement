package com.acme.health.multitenant.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MultiTenantProperties captures configuration options for multi-tenant support.
 */
@ConfigurationProperties(prefix = "multitenant")
public class MultiTenantProperties {

    private boolean enabled = true;

    @Valid
    @NotNull
    private ResolutionProperties resolution = new ResolutionProperties();

    @Valid
    @NotNull
    private StoreProperties store = new StoreProperties();

    /**
     * Indicates whether the multi-tenant features are enabled.
     *
     * @return true if enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled flag.
     *
     * @param enabled the enabled flag
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets the resolution properties.
     *
     * @return the resolution properties
     */
    public ResolutionProperties getResolution() {
        return resolution;
    }

    /**
     * Sets the resolution properties.
     *
     * @param resolution the properties
     */
    public void setResolution(final ResolutionProperties resolution) {
        this.resolution = resolution;
    }

    /**
     * Gets the store properties.
     *
     * @return the store properties
     */
    public StoreProperties getStore() {
        return store;
    }

    /**
     * Sets the store properties.
     *
     * @param store the store properties
     */
    public void setStore(final StoreProperties store) {
        this.store = store;
    }

    /**
     * ResolutionProperties describes tenant resolution configuration.
     */
    public static class ResolutionProperties {

        private boolean hostEnabled = true;
        private boolean headerEnabled = true;
        @NotBlank
        private String headerName = "X-Tenant-Id";
        @NotEmpty
        private List<String> trustedTenants = new ArrayList<>();

        /**
         * Indicates whether host-based resolution is enabled.
         *
         * @return true if enabled
         */
        public boolean isHostEnabled() {
            return hostEnabled;
        }

        /**
         * Sets host-based resolution flag.
         *
         * @param hostEnabled true if enabled
         */
        public void setHostEnabled(final boolean hostEnabled) {
            this.hostEnabled = hostEnabled;
        }

        /**
         * Indicates whether header-based resolution is enabled.
         *
         * @return true if enabled
         */
        public boolean isHeaderEnabled() {
            return headerEnabled;
        }

        /**
         * Sets header-based resolution flag.
         *
         * @param headerEnabled true if enabled
         */
        public void setHeaderEnabled(final boolean headerEnabled) {
            this.headerEnabled = headerEnabled;
        }

        /**
         * Gets the header name used for resolution.
         *
         * @return the header name
         */
        public String getHeaderName() {
            return headerName;
        }

        /**
         * Sets the header name for resolution.
         *
         * @param headerName the header name
         */
        public void setHeaderName(final String headerName) {
            this.headerName = headerName;
        }

        /**
         * Gets the trusted tenant identifiers.
         *
         * @return the trusted tenant identifiers
         */
        public List<String> getTrustedTenants() {
            return trustedTenants;
        }

        /**
         * Sets the trusted tenant identifiers.
         *
         * @param trustedTenants the trusted tenant identifiers
         */
        public void setTrustedTenants(final List<String> trustedTenants) {
            this.trustedTenants = trustedTenants;
        }
    }

    /**
     * StoreProperties defines configuration store behaviour.
     */
    public static class StoreProperties {

        private StoreType type = StoreType.PROFILE;

        @Valid
        @NotNull
        private CacheProperties cache = new CacheProperties();

        @Valid
        @NotNull
        private DefaultStore defaults = new DefaultStore();

        @Valid
        @NotEmpty
        private List<TenantConfiguration> tenants = new ArrayList<>();

        /**
         * Gets the store type.
         *
         * @return the store type
         */
        public StoreType getType() {
            return type;
        }

        /**
         * Sets the store type.
         *
         * @param type the store type
         */
        public void setType(final StoreType type) {
            this.type = type;
        }

        /**
         * Gets the cache properties.
         *
         * @return the cache properties
         */
        public CacheProperties getCache() {
            return cache;
        }

        /**
         * Sets the cache properties.
         *
         * @param cache the cache properties
         */
        public void setCache(final CacheProperties cache) {
            this.cache = cache;
        }

        /**
         * Gets the default store configuration.
         *
         * @return the default store configuration
         */
        public DefaultStore getDefaults() {
            return defaults;
        }

        /**
         * Sets the default configuration.
         *
         * @param defaults the defaults
         */
        public void setDefaults(final DefaultStore defaults) {
            this.defaults = defaults;
        }

        /**
         * Gets the tenant configurations.
         *
         * @return the tenant configurations
         */
        public List<TenantConfiguration> getTenants() {
            return tenants;
        }

        /**
         * Sets the tenant configurations.
         *
         * @param tenants the tenant configurations
         */
        public void setTenants(final List<TenantConfiguration> tenants) {
            this.tenants = tenants;
        }
    }

    /**
     * CacheProperties describes cache settings for tenant lookups.
     */
    public static class CacheProperties {

        private boolean enabled = true;
        private Duration ttl = Duration.ofMinutes(15);

        /**
         * Indicates whether cache is enabled.
         *
         * @return true if enabled
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets cache enabled flag.
         *
         * @param enabled true if enabled
         */
        public void setEnabled(final boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * Gets the entry time-to-live.
         *
         * @return the TTL duration
         */
        public Duration getTtl() {
            return ttl;
        }

        /**
         * Sets the cache TTL.
         *
         * @param ttl the TTL duration
         */
        public void setTtl(final Duration ttl) {
            this.ttl = ttl;
        }
    }

    /**
     * StoreType enumerates supported configuration stores.
     */
    public enum StoreType {
        IN_MEMORY,
        PROFILE
    }

    /**
     * DefaultStore captures default tenant connection details.
     */
    public static class DefaultStore {

        @NotBlank
        private String connectionString = "jdbc:h2:mem:default";

        /**
         * Gets the default connection string.
         *
         * @return the connection string
         */
        public String getConnectionString() {
            return connectionString;
        }

        /**
         * Sets the default connection string.
         *
         * @param connectionString the connection string
         */
        public void setConnectionString(final String connectionString) {
            this.connectionString = connectionString;
        }
    }

    /**
     * TenantConfiguration describes a tenant entry loaded from configuration.
     */
    public static class TenantConfiguration {

        @NotBlank
        private String id;

        @NotBlank
        private String identifier;

        @NotBlank
        private String name;

        @NotBlank
        private String connectionString;

        private Map<String, Object> properties;

        /**
         * Gets the tenant id.
         *
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * Sets the tenant id.
         *
         * @param id the id
         */
        public void setId(final String id) {
            this.id = id;
        }

        /**
         * Gets the tenant identifier.
         *
         * @return the identifier
         */
        public String getIdentifier() {
            return identifier;
        }

        /**
         * Sets the tenant identifier.
         *
         * @param identifier the identifier
         */
        public void setIdentifier(final String identifier) {
            this.identifier = identifier;
        }

        /**
         * Gets the tenant name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the tenant name.
         *
         * @param name the name
         */
        public void setName(final String name) {
            this.name = name;
        }

        /**
         * Gets the connection string.
         *
         * @return the connection string
         */
        public String getConnectionString() {
            return connectionString;
        }

        /**
         * Sets the connection string.
         *
         * @param connectionString the connection string
         */
        public void setConnectionString(final String connectionString) {
            this.connectionString = connectionString;
        }

        /**
         * Gets the arbitrary properties.
         *
         * @return the properties
         */
        public Map<String, Object> getProperties() {
            return properties;
        }

        /**
         * Sets the arbitrary properties.
         *
         * @param properties the properties
         */
        public void setProperties(final Map<String, Object> properties) {
            this.properties = properties;
        }
    }
}

