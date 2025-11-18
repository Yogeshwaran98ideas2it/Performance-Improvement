package com.acme.healthcare.service.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * RolePermissionRequest captures incoming payload for role permission creation and updates.
 */
public class RolePermissionRequest {

    @NotBlank
    private String code;

    private String description;

    /**
     * Gets the permission code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the permission code.
     *
     * @param code the code
     */
    public void setCode(final String code) {
        this.code = code;
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
}







