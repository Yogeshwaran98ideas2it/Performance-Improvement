package com.acme.healthcare.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Refresh token request payload.
 */
@Data
public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;
}









