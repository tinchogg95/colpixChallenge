package com.colpix.challenge.controller;

import com.colpix.challenge.dto.LoginRequest;
import com.colpix.challenge.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private JwtUtil jwtUtil;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        authController = new AuthController(jwtUtil);
    }

    @Test
    void testLoginWithValidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("1234");

        String mockToken = "mocked-jwt-token";
        when(jwtUtil.generateToken("admin")).thenReturn(mockToken);

        ResponseEntity<String> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("{\"token\": \"" + mockToken + "\"}", response.getBody());
        verify(jwtUtil, times(1)).generateToken("admin");
    }

    @Test
    void testLoginWithInvalidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setUsername("user");
        request.setPassword("wrong");
        ResponseEntity<String> response = authController.login(request);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Credenciales inválidas", response.getBody());
        verify(jwtUtil, never()).generateToken(anyString());
    }
}
