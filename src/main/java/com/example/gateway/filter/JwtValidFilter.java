package com.example.gateway.filter;

import com.example.gateway.global.error.exception.token.TokenValidException;
import com.example.gateway.global.jwt.JwtVerify;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtValidFilter extends AbstractGatewayFilterFactory<JwtValidFilter.Config> {

    @Autowired
    private JwtVerify jwtVerify;

    public static class Config {}

    public JwtValidFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();


//            log.info("    [JWT 검증 필터] REQUEST >>> IP : {}, URI : {}", request.getRemoteAddress().getAddress(), request.getURI());
//            request.getHeaders().forEach((key, value) -> {
//                log.info("        [요청 Header] {} : {}", key, value);
//            });


            String jwt = getJwt(request);
            isAvailable(jwt);


            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//                log.info("        -----------------------------------------------------------------");
//                response.getHeaders().forEach((key, value) -> {
//                    log.info("        [응답 Header] {} : {}", key, value);
//                });
//
//                log.info("    [JWT 검증 필터] RESPONSE >>> IP : {}, URI : {}, Status : {}",
//                        request.getRemoteAddress().getAddress(),
//                        request.getURI(),
//                        response.getStatusCode());
            }));
        }));
    }

    private String getJwt(ServerHttpRequest request) {
        String jwt = request.getHeaders().getFirst("Authorization");

        if (jwt == null)
            throw new TokenValidException("토큰이 존재하지 않음");

        return jwt;
    }

    private void isAvailable(String jwt) {
        jwtVerify.verify(jwt);
    }
}