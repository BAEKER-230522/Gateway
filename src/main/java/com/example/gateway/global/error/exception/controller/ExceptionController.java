package com.example.gateway.global.error.exception.controller;

import com.example.gateway.global.error.ErrorMsg;
import com.example.gateway.global.error.exception.TokenExpiredException;
import com.example.gateway.global.error.exception.TokenValidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(TokenValidException.class)
    public Mono<ResponseEntity<ErrorMsg>> tokenValidExceptionHandler(TokenValidException e) {
        log.error(e.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMsg(e.getMessage())));
    }

    @ExceptionHandler(TokenExpiredException.class)
    public Mono<ResponseEntity<ErrorMsg>> tokenExpiredExceptionHandler(TokenExpiredException e) {
        log.error(e.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorMsg(e.getMessage())));
    }
}
