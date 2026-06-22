package com.gateway.api_gateway.security.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRateLimiterFilter implements GlobalFilter, Ordered {

    private final Map<String, TokenBucket> buckets = new ConcurrentHashMap<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String ip = exchange.getRequest().getRemoteAddress() != null
                ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                : "unknown";

        String path = exchange.getRequest().getURI().getPath();

        // Tentukan limit berdasarkan path
        // Jika endpoint login, verify-otp, atau resend-otp, batasi lebih ketat (brute force protection)
        double capacity;
        double refillRate; // token per detik

        if (path.contains("/auth/login") || path.contains("/auth/verify-otp") || path.contains("/auth/resend-otp")) {
            // Tighter limit: 3 requests burst, 1 request per 2 seconds (0.5 per sec)
            capacity = 3.0;
            refillRate = 0.5; 
        } else {
            // General limit: 15 requests burst, 5 requests per second
            capacity = 15.0;
            refillRate = 5.0;
        }

        // Dapatkan atau buat bucket baru untuk kombinasi IP & Tipe Route (auth / gen)
        String bucketKey = ip + ":" + (path.contains("/auth") ? "auth" : "gen");
        TokenBucket bucket = buckets.computeIfAbsent(bucketKey, 
                k -> new TokenBucket(capacity, refillRate));

        if (!bucket.tryConsume()) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            exchange.getResponse().getHeaders().add("Content-Type", "application/json");
            
            byte[] bytes = "{\"status\":429,\"message\":\"Too Many Requests\",\"error\":\"Rate limit exceeded. Silakan coba beberapa saat lagi.\"}"
                    .getBytes();
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // Jalankan sebelum filter lainnya (-100) untuk meminimalkan beban CPU sebelum token validasi
        return -100;
    }

    private static class TokenBucket {
        private final double capacity;
        private final double refillRate;
        private double tokens;
        private long lastRefillTime;

        public TokenBucket(double capacity, double refillRate) {
            this.capacity = capacity;
            this.refillRate = refillRate;
            this.tokens = capacity;
            this.lastRefillTime = System.currentTimeMillis();
        }

        public synchronized boolean tryConsume() {
            refill();
            if (tokens >= 1.0) {
                tokens -= 1.0;
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            double elapsedSeconds = (now - lastRefillTime) / 1000.0;
            if (elapsedSeconds > 0) {
                tokens = Math.min(capacity, tokens + elapsedSeconds * refillRate);
                lastRefillTime = now;
            }
        }
    }
}
