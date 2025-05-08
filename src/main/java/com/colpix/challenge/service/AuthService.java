package com.colpix.challenge.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import com.colpix.challenge.dto.LoginRequest;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    

    private final long EXPIRATION_TIME = 300_000; // 5 minutos en milisegundos
    

    private final Map<String, String> users = new HashMap<>() {{
        put("admin", "admin123");
        put("user", "user123");
    }};
    
    public String authenticate(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        
        // Verificar credenciales
        if (users.containsKey(username) && users.get(username).equals(password)) {
            // Generar token JWT
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(SECRET_KEY)
                    .compact();
        }
        return null;
    }

    public SecretKey getSecretKey() {
        return this.SECRET_KEY;
    }
}