package com.clinic.auth.security.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.clinic.auth.exception.UserNotFoundException;
import com.clinic.auth.security.jwt.JwtService;
import com.clinic.auth.user.entity.UsersEntity;
import com.clinic.auth.user.repository.UsersRepository;

import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UsersRepository usersRepository;

    @Value("${internal.security.token:SecretInternalToken123}")
    private String internalToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/auth/internal/")) {
            String headerToken = request.getHeader("X-Internal-Token");
            if (internalToken.equals(headerToken)) {
                filterChain.doFilter(request, response);
                return;
            }
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request,response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                UsersEntity user = usersRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException("User not found"));

                if (jwtService.isTokenValid(token, user)) {

                    List<GrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority(
                                    "ROLE_" + user.getRole().getRoleName()));

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            authorities);

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authToken);
                }
            }
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}