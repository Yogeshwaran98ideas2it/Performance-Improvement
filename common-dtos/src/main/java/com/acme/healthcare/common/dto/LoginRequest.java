package com.acme.healthcare.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Login request payload shared between auth-capable services.
 */
@Data
public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}










