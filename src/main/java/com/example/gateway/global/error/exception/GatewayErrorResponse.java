package com.example.gateway.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class GatewayErrorResponse {
    private final String errorMessage;
    private final LocalDateTime localDateTime;
    private final Map<String, Object> errorInfo;

    public GatewayErrorResponse(String errorMessage, LocalDateTime localDateTime) {
        this.errorMessage = errorMessage;
        this.localDateTime = localDateTime;
        this.errorInfo = new HashMap<>();
    }

    public static GatewayErrorResponse defaultError(String errorMessage){
        return new GatewayErrorResponse(errorMessage, LocalDateTime.now());
    }
}
