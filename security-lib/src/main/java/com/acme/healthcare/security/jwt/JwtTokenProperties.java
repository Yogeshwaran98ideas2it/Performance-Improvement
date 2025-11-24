package com.acme.healthcare.security.jwt;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for JWT token issuance and validation.
 */
@Validated
@ConfigurationProperties(prefix = "security.jwt")
public class JwtTokenProperties {

    @NotBlank
    private String issuer;

    @Min(1)
    private long accessTokenTtlMinutes;

    @Min(1)
    private long refreshTokenTtlHours;

    @NotBlank
    private String secret;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }

    public long getAccessTokenTtlMinutes() {
        return accessTokenTtlMinutes;
    }

    public void setAccessTokenTtlMinutes(final long accessTokenTtlMinutes) {
        this.accessTokenTtlMinutes = accessTokenTtlMinutes;
    }

    public long getRefreshTokenTtlHours() {
        return refreshTokenTtlHours;
    }

    public void setRefreshTokenTtlHours(final long refreshTokenTtlHours) {
        this.refreshTokenTtlHours = refreshTokenTtlHours;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(final String secret) {
        this.secret = secret;
    }
}









