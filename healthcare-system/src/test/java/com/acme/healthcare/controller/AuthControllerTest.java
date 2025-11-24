package com.acme.healthcare.controller;

import com.acme.healthcare.service.AuthService;
import com.acme.healthcare.service.dto.LoginRequest;
import com.acme.healthcare.service.dto.RefreshTokenRequest;
import com.acme.healthcare.service.dto.TokenResponse;
import com.acme.healthcare.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link AuthController}.
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Test
    void login_ShouldReturnTokens() throws Exception {
        TokenResponse response = new TokenResponse();
        response.setAccessToken("access");
        response.setRefreshToken("refresh");
        when(authService.authenticate(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"username":"jdoe","password":"secret"}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("access"))
            .andExpect(jsonPath("$.refreshToken").value("refresh"));

        verify(authService).authenticate(any(LoginRequest.class));
    }

    @Test
    void refresh_ShouldReturnNewTokens() throws Exception {
        TokenResponse response = new TokenResponse();
        response.setAccessToken("new-access");
        response.setRefreshToken("new-refresh");
        when(authService.refresh(any(RefreshTokenRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"refreshToken":"token"}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("new-access"));

        verify(authService).refresh(any(RefreshTokenRequest.class));
    }
}










