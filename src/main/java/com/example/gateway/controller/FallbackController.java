package com.example.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {
    private static final Logger log = LoggerFactory.getLogger(FallbackController.class);

    @GetMapping("/fallback-rule")
    public Mono<ResponseEntity<String>> fallback() {
        log.info("Fallback-rule");
        return Mono.just(
                ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service is currently unavailable. Please try again later.")
        );
    }
}
