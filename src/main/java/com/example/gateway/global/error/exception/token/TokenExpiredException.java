package com.example.gateway.global.error.exception.token;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String msg) {
        super(msg);
    }
}
