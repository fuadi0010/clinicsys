package com.gateway.api_gateway.error;

import java.time.LocalDateTime;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalErrorWebExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = determineHttpStatus(ex);
        String message = determineMessage(ex, status);

        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(status.value())
                .message(message)
                .timeStamp(LocalDateTime.now())
                .build();

        ServerHttpResponse httpResponse = exchange.getResponse();
        httpResponse.setStatusCode(status);
        httpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(response);
            DataBuffer buffer = httpResponse.bufferFactory().wrap(bytes);
            return httpResponse.writeWith(Mono.just(buffer));
        } catch (Exception e) {
            return httpResponse.setComplete();
        }
    }

    private HttpStatus determineHttpStatus(Throwable ex) {
        if (ex instanceof ResponseStatusException) {
            HttpStatusCode statusCode = ((ResponseStatusException) ex).getStatusCode();
            if (statusCode instanceof HttpStatus) {
                return (HttpStatus) statusCode;
            }
            return HttpStatus.resolve(statusCode.value());
        }
        if (ex instanceof SecurityException) {
            return HttpStatus.UNAUTHORIZED;
        }
        if (ex instanceof IllegalArgumentException) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private String determineMessage(Throwable ex, HttpStatus status) {
        if (ex instanceof ResponseStatusException) {
            return ((ResponseStatusException) ex).getReason();
        }
        if (ex.getMessage() != null && !ex.getMessage().isBlank()) {
            return ex.getMessage();
        }
        return status.getReasonPhrase();
    }
}
