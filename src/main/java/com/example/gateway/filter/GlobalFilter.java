package com.example.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {


    public GlobalFilter() {
        super(GlobalFilter.Config.class);
    }


    public static class Config {}


    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

//            log.info("[글로벌 필터] REQUEST >>> IP : {}, URI : {}", request.getRemoteAddress().getAddress(), request.getURI());


            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                log.info("[글로벌 필터] RESPONSE >>> IP : {}, URI : {}, Status : {}",
//                        request.getRemoteAddress().getAddress(),
//                        request.getURI(),
//                        response.getStatusCode()
//                );
            }));
        }));
    }
}
