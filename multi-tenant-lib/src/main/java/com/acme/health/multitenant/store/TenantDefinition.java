package com.acme.health.multitenant.store;

import java.util.Map;
import java.util.Objects;

/**
 * TenantDefinition holds metadata and connection details for a tenant.
 */
public class TenantDefinition {

    private String id;
    private String identifier;
    private String name;
    private String connectionString;
    private Map<String, Object> properties;

    /**
     * Gets the database id.
     *
     * @return the identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
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
     * Gets arbitrary properties.
     *
     * @return the properties
     */
    public Map<String, Object> getProperties() {
        return properties;
    }

    /**
     * Sets arbitrary properties.
     *
     * @param properties the properties
     */
    public void setProperties(final Map<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * Checks equality based on identifier.
     *
     * @param o the other object
     * @return true if identifiers match
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TenantDefinition that = (TenantDefinition) o;
        return Objects.equals(identifier, that.identifier);
    }

    /**
     * Computes hash code using identifier.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
}


