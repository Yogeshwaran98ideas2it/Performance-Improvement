package com.acme.healthcare.service;

import com.acme.healthcare.service.dto.LoginRequest;
import com.acme.healthcare.service.dto.RefreshTokenRequest;
import com.acme.healthcare.service.dto.TokenResponse;

/**
 * AuthService coordinates authentication functionality.
 */
public interface AuthService {

    /**
     * Authenticates the user and returns tokens.
     *
     * @param request the login request
     * @return the token response
     */
    TokenResponse authenticate(LoginRequest request);

    /**
     * Refreshes the access token.
     *
     * @param request the refresh request
     * @return the new token response
     */
    TokenResponse refresh(RefreshTokenRequest request);
}










