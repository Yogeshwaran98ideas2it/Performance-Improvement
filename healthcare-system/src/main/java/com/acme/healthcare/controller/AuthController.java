package com.acme.healthcare.controller;

import com.acme.healthcare.service.AuthService;
import com.acme.healthcare.service.dto.LoginRequest;
import com.acme.healthcare.service.dto.RefreshTokenRequest;
import com.acme.healthcare.service.dto.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController exposes authentication endpoints.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Creates the controller.
     *
     * @param authService the authentication service
     */
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates the user and issues tokens.
     *
     * @param request the login request
     * @return the token response
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody final LoginRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    /**
     * Refreshes tokens using a refresh token.
     *
     * @param request the refresh request
     * @return the refreshed token response
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody final RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }
}


