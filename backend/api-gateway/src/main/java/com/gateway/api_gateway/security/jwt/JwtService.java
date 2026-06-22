package com.gateway.api_gateway.security.jwt;

public interface JwtService {
    boolean isTokenValid( String token );
}
