package com.clinic.appointment.security.jwt.implement;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.clinic.appointment.security.jwt.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtServiceImpl implements JwtService{
    @Value("${jwt.secret-key}")
    private String secretKey;
    private Key signingKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        Number userId = claims.get("userId", Number.class);
        return userId.longValue();
    }

    @Override
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith((javax.crypto.SecretKey) signingKey).build().parseSignedClaims(token)
                .getPayload();
    }
}
