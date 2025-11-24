package com.acme.healthcare.service.impl;

import com.acme.healthcare.domain.entity.RefreshToken;
import com.acme.healthcare.domain.entity.Role;
import com.acme.healthcare.domain.entity.User;
import com.acme.healthcare.domain.repository.RefreshTokenRepository;
import com.acme.healthcare.domain.repository.UserRepository;
import com.acme.healthcare.domain.enums.RoleType;
import com.acme.healthcare.security.jwt.JwtTokenProperties;
import com.acme.healthcare.security.jwt.JwtTokenProvider;
import com.acme.healthcare.service.dto.LoginRequest;
import com.acme.healthcare.service.dto.RefreshTokenRequest;
import com.acme.healthcare.service.dto.TokenResponse;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anySet;

/**
 * Unit tests for {@link AuthServiceImpl}.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProperties tokenProperties;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;
    private LoginRequest loginRequest;
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setName(RoleType.PHYSICIAN);

        user = new User();
        user.setId(7L);
        user.setUsername("jdoe");
        user.setRoles(Set.of(role));

        loginRequest = new LoginRequest();
        loginRequest.setUsername("jdoe");
        loginRequest.setPassword("secret");

        refreshToken = new RefreshToken();
        refreshToken.setId(11L);
        refreshToken.setToken("refresh-123");
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plusSeconds(3600));
        refreshToken.setRevoked(false);

        when(tokenProperties.getAccessTokenTtlMinutes()).thenReturn(30L);
        when(tokenProperties.getRefreshTokenTtlHours()).thenReturn(12L);
    }

    @Test
    void authenticate_ShouldIssueTokens() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(user));
        when(tokenProvider.generateAccessToken(any(), anySet())).thenReturn("access-token");
        when(tokenProvider.generateRefreshToken()).thenReturn("refresh-token");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);

        TokenResponse result = authService.authenticate(loginRequest);

        assertNotNull(result);
        assertEquals("access-token", result.getAccessToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void authenticate_WithInvalidCredentials_ShouldThrow() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("bad"));

        assertThrows(BadCredentialsException.class, () -> authService.authenticate(loginRequest));
        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    void refresh_ShouldReturnNewAccessToken() {
        when(refreshTokenRepository.findByTokenAndRevokedFalse("refresh-123")).thenReturn(Optional.of(refreshToken));
        when(tokenProvider.generateAccessToken(any(), anySet())).thenReturn("new-access");
        when(tokenProvider.generateRefreshToken()).thenReturn("new-refresh");
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-123");

        TokenResponse result = authService.refresh(request);

        assertEquals("new-access", result.getAccessToken());
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void refresh_WithExpiredToken_ShouldMarkRevokedAndThrow() {
        refreshToken.setExpiresAt(Instant.now().minusSeconds(5));
        when(refreshTokenRepository.findByTokenAndRevokedFalse("refresh-123")).thenReturn(Optional.of(refreshToken));

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-123");

        assertThrows(IllegalStateException.class, () -> authService.refresh(request));
        verify(refreshTokenRepository, times(1)).save(refreshToken);
    }

    @Test
    void refresh_WithUnknownToken_ShouldThrow() {
        when(refreshTokenRepository.findByTokenAndRevokedFalse("missing")).thenReturn(Optional.empty());

        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("missing");

        assertThrows(EntityNotFoundException.class, () -> authService.refresh(request));
    }
}

