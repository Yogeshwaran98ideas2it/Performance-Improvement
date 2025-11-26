package com.acme.healthcare.service.dto;

import java.util.Set;

/**
 * RoleResponse represents role data returned to clients.
 */
public class RoleResponse {

    private Long id;
    private String name;
    private String description;
    private Set<RolePermissionResponse> permissions;

    /**
     * Gets the identifier.
     *
     * @return the identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the identifier.
     *
     * @param id the identifier
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the permissions.
     *
     * @return the permissions
     */
    public Set<RolePermissionResponse> getPermissions() {
        return permissions;
    }

    /**
     * Sets the permissions.
     *
     * @param permissions the permissions
     */
    public void setPermissions(final Set<RolePermissionResponse> permissions) {
        this.permissions = permissions;
    }
}











