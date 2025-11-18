package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.RefreshToken;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.repository.RefreshTokenRepository;
import com.acme.healthcare.domain.repository.UserRepository;
import com.acme.healthcare.security.jwt.JwtTokenProperties;
import com.acme.healthcare.security.jwt.JwtTokenProvider;
import com.acme.healthcare.service.AuthService;
import com.acme.healthcare.service.dto.LoginRequest;
import com.acme.healthcare.service.dto.RefreshTokenRequest;
import com.acme.healthcare.service.dto.TokenResponse;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthServiceImpl executes authentication and token workflows.
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtTokenProperties properties;

    /**
     * Creates the authentication service.
     *
     * @param authenticationManager the authentication manager
     * @param tokenProvider the token provider
     * @param refreshTokenRepository the refresh token repository
     * @param userRepository the user repository
     * @param properties the token properties
     */
    public AuthServiceImpl(final AuthenticationManager authenticationManager,
                           final JwtTokenProvider tokenProvider,
                           final RefreshTokenRepository refreshTokenRepository,
                           final UserRepository userRepository,
                           final JwtTokenProperties properties) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.properties = properties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TokenResponse authenticate(final LoginRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword());
        Authentication authenticated = authenticationManager.authenticate(authentication);
        // Reload user within this transaction to ensure all relationships are loaded
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        // Force initialization of collections while in transaction
        user.getRoles().size();
        user.getRoles().forEach(role -> role.getPermissions().size());
        return issueTokens(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenResponse refresh(final RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndRevokedFalse(request.getRefreshToken())
            .orElseThrow(() -> new EntityNotFoundException("Refresh token not found"));
        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new IllegalStateException("Refresh token expired");
        }
        return issueTokens(refreshToken.getUser());
    }

    private TokenResponse issueTokens(final User user) {
        Set<String> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add("ROLE_" + role.getName());
            role.getPermissions().forEach(permission -> authorities.add(permission.getCode()));
        });
        String accessToken = tokenProvider.generateAccessToken(user.getUsername(), authorities);
        String refreshTokenValue = tokenProvider.generateRefreshToken();
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(refreshTokenValue);
        refreshToken.setExpiresAt(Instant.now().plusSeconds(properties.getRefreshTokenTtlHours() * 3600));
        refreshTokenRepository.save(refreshToken);

        TokenResponse response = new TokenResponse();
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshTokenValue);
        response.setExpiresIn(properties.getAccessTokenTtlMinutes() * 60);
        return response;
    }
}


