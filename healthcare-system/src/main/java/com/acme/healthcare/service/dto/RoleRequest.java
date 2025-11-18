package com.acme.healthcare.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * RoleRequest represents role creation and update payloads.
 */
public class RoleRequest {

    @NotBlank
    private String name;

    private String description;

    @NotEmpty
    private Set<Long> permissionIds;

    /**
     * Gets the role name.
     *
     * @return the role name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the role name.
     *
     * @param name the role name
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
     * Gets the permission identifiers.
     *
     * @return the permission identifiers
     */
    public Set<Long> getPermissionIds() {
        return permissionIds;
    }

    /**
     * Sets the permission identifiers.
     *
     * @param permissionIds the permission identifiers
     */
    public void setPermissionIds(final Set<Long> permissionIds) {
        this.permissionIds = permissionIds;
    }
}







