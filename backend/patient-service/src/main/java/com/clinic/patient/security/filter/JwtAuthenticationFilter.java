package com.clinic.patient.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.clinic.patient.security.jwt.JwtService;

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
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // INTERNAL ENDPOINT BYPASS WITH SECRET TOKEN
        if (path.startsWith("/patients/internal/")) {
            String headerToken = request.getHeader("X-Internal-Token");
            if (internalToken.equals(headerToken)) {
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String authHeader = request.getHeader(
                HttpHeaders.AUTHORIZATION);

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(
                    request,
                    response);

            return;
        }

        String token = authHeader.substring(7);

        try {

            Long userId = jwtService.extractUserId(token);

            String role = jwtService.extractRole(token);

            if (userId != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userId,
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + role)));

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }

        } catch (Exception ex) {

            SecurityContextHolder.clearContext();

            ex.printStackTrace();
        }

        filterChain.doFilter(
                request,
                response);
    }

}
