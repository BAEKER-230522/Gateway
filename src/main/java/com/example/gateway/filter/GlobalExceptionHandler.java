package com.example.gateway.filter;

import com.example.gateway.global.error.exception.GatewayErrorResponse;
import com.example.gateway.global.error.exception.token.TokenValidException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Order(-1)
@RequiredArgsConstructor
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // Header
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof TokenValidException) {
            response.setStatusCode(((HttpStatus.UNAUTHORIZED).is4xxClientError()) ? HttpStatus.UNAUTHORIZED : HttpStatus.INTERNAL_SERVER_ERROR);
            System.out.println("sssss");
        }
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                System.out.println("sasasa");
                GatewayErrorResponse gwErrorResponse = GatewayErrorResponse.defaultError(ex.getMessage());
                byte[] errorResponse = objectMapper.writeValueAsBytes(gwErrorResponse);

                return bufferFactory.wrap(errorResponse);
            } catch (Exception e) {
                log.error("error", e);
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
