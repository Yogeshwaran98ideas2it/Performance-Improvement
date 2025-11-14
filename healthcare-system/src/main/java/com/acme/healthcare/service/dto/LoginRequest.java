package com.acme.healthcare.service.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * LoginRequest captures authentication credentials.
 */
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username the username
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the password
     */
    public void setPassword(final String password) {
        this.password = password;
    }
}


