package com.acme.healthcare.service.dto;

/**
 * TokenResponse returns JWT tokens to the client.
 */
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
    private long expiresIn;

    /**
     * Gets the access token.
     *
     * @return the access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Sets the access token.
     *
     * @param accessToken the access token
     */
    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

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

    /**
     * Gets the expiration in seconds.
     *
     * @return the expiration
     */
    public long getExpiresIn() {
        return expiresIn;
    }

    /**
     * Sets the expiration in seconds.
     *
     * @param expiresIn the expiration
     */
    public void setExpiresIn(final long expiresIn) {
        this.expiresIn = expiresIn;
    }
}







