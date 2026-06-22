package com.gateway.api_gateway.error;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ApiErrorResponse {
    private Integer status;
    private String message;
    private LocalDateTime timeStamp;
}
