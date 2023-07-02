package com.example.gateway.filter;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    private StopWatch stopWatch;

    public static class Config{}

    public GlobalFilter() {
        super(GlobalFilter.Config.class);
        stopWatch = new StopWatch("API Gatway");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {

            // Req , Res 객체 가져오기
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            // Request 요청시 최초로 실행되는 필터
//            stopWatch.start();
            log.info("[Filter] REQUEST >>> IP : {}, URI : {}", request.getRemoteAddress(), request.getURI());

            // POST 필터
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {

//                stopWatch.stop();
                log.info("[Filter] RESPONSE >>> IP : {}, URI : {}, Status : {} ---> Work Time : -- ms",
                        request.getRemoteAddress().getAddress(),
                        request.getURI(),
                        response.getStatusCode()
//                        stopWatch.getLastTaskTimeMillis()
                );
            }));
        }));
    }
}
