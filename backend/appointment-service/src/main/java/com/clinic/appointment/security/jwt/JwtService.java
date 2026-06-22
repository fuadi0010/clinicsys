package com.clinic.appointment.security.jwt;

public interface JwtService {
    Long extractUserId(String token);

    String extractRole(String token);

    boolean isTokenValid(String token);
}
