package com.clinic.auth.security.jwt;

import com.clinic.auth.user.entity.UsersEntity;

public interface JwtService {
    String generateToken(UsersEntity user);
    String extractUsername(String token);
    String extractRole(String token);
    Long extractUserId(String token);
    boolean isTokenValid(String token, UsersEntity user);
}
