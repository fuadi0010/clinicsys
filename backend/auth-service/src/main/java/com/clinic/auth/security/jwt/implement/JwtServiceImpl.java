package com.clinic.auth.security.jwt.implement;

import com.clinic.auth.security.jwt.JwtService;
import com.clinic.auth.user.entity.UsersEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    private SecretKey signingKey;

    @PostConstruct
    public void init(){

        byte[] keyBytes =
                Decoders.BASE64.decode(
                        secretKey
                );

        this.signingKey =
                Keys.hmacShaKeyFor(
                        keyBytes
                );
    }

    @Override
    public String generateToken(UsersEntity user) {

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId",user.getId())
                .claim("role",user.getRole().getRoleName())
                .claim("isVerified", user.getIsVerified() != null && user.getIsVerified())
                .claim("email", user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+ expiration))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    @Override
    public String extractRole(String token) {
        return extractAllClaims(token)
                .get("role", String.class);
    }

    @Override
    public Long extractUserId(String token){

        return extractAllClaims(token).get("userId",Long.class);
    }

    @Override
    public boolean isTokenValid(String token, UsersEntity user){
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && 
                        !extractAllClaims(token) .getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token){

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
