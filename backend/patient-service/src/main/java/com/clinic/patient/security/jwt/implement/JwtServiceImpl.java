package com.clinic.patient.security.jwt.implement;

import com.clinic.patient.security.jwt.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret-key}")
    private String secretKey;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String extractUsername(String token) {

        return extractAllClaims(token).getSubject();
    }

    @Override
    public Long extractUserId(String token) {

        return extractAllClaims(token)
                .get("userId", Long.class);
    }

    @Override
    public String extractRole(String token) {

        return extractAllClaims(token)
                .get("role",String.class);
    }

    @Override
    public boolean isTokenValid(String token) {

        return !extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
