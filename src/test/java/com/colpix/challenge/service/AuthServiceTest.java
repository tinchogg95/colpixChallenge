package com.colpix.challenge.service;

import com.colpix.challenge.dto.LoginRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService();
    }

    @Test
    void testAuthenticateWithValidAdminCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin123");

        String token = authService.authenticate(loginRequest);

        assertNotNull(token);
        Claims claims = parseToken(token, authService.getSecretKey());
        assertEquals("admin", claims.getSubject());
    }

    @Test
    void testAuthenticateWithValidUserCredentials() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("user");
        loginRequest.setPassword("user123");

        String token = authService.authenticate(loginRequest);

        assertNotNull(token);
        Claims claims = parseToken(token, authService.getSecretKey());
        assertEquals("user", claims.getSubject());
    }

    @Test
    void testAuthenticateWithInvalidPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("wrongpassword");

        String token = authService.authenticate(loginRequest);

        assertNull(token);
    }

    @Test
    void testAuthenticateWithUnknownUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistent");
        loginRequest.setPassword("123");

        String token = authService.authenticate(loginRequest);

        assertNull(token);
    }

    private Claims parseToken(String token, SecretKey secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
