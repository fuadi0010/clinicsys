package com.clinic.medical_record.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.clinic.medical_record.security.jwt.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Value("${internal.security.token:SecretInternalToken123}")
    private String internalToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                System.out.println( request.getMethod() + " " + request.getServletPath() );
        String path = request.getServletPath();
        if (path.startsWith("/medical-records/internal/")) {
            String headerToken = request.getHeader("X-Internal-Token");
            if (internalToken.equals(headerToken)) {
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        try {
            Long userId = jwtService.extractUserId(token);
            String role = jwtService.extractRole(token);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role)));
                    System.out.println("ROLE = " + role); System.out.println( authToken.getAuthorities() );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (Exception exception) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
