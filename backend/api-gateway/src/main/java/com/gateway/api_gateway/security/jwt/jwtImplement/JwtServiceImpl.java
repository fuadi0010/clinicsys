package com.gateway.api_gateway.security.jwt.jwtImplement;

import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.gateway.api_gateway.security.jwt.JwtService;
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
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith((javax.crypto.SecretKey) signingKey).build().parseSignedClaims(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
