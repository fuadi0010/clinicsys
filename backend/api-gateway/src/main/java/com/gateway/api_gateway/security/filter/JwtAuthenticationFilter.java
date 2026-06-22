package com.gateway.api_gateway.security.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.gateway.api_gateway.security.jwt.JwtService;
import reactor.core.publisher.Mono;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter {
    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String rawPath = exchange.getRequest().getURI().getRawPath();

        // Proteksi endpoint internal dari akses eksternal melalui Gateway
        if (path.matches(".*/internal/.*") || rawPath.matches(".*/internal/.*")) {
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
            return exchange.getResponse().setComplete();
        }

        // Bypass autentikasi untuk endpoint publik
        if (path.startsWith("/api/auth") || path.startsWith("/auth") || path.startsWith("/api/auth/") || path.startsWith("/api/v1/contact-messages")) {
            return chain.filter(exchange);
        }
        if (path.contains("/uploads/") && "GET".equalsIgnoreCase(exchange.getRequest().getMethod().name())) {
            return chain.filter(exchange);
        }
        if (path.contains("/appointments/simulate-pay/")) {
            return chain.filter(exchange);
        }
        if ((path.startsWith("/api/doctors") || path.startsWith("/doctors"))
                && "GET".equalsIgnoreCase(exchange.getRequest().getMethod().name())) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
}
