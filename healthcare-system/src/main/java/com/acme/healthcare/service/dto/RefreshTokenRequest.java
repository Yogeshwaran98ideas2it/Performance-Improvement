package com.acme.healthcare.service.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * RefreshTokenRequest carries refresh token credentials.
 */
public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;

    /**
     * Gets the refresh token.
     *
     * @return the refresh token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * Sets the refresh token.
     *
     * @param refreshToken the refresh token
     */
    public void setRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}


