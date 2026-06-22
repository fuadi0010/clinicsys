package com.clinic.medical_record.security.jwt;

public interface JwtService {
    Long extractUserId(String token);

    String extractRole(String token);

    boolean isTokenValid(String token);
}
