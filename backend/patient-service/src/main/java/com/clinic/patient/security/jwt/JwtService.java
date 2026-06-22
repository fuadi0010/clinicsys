package com.clinic.patient.security.jwt;

public interface JwtService {
    String extractUsername(String token);
    Long extractUserId(String token);
    String extractRole(String token);
    boolean isTokenValid(String token);
}
