package com.example.gateway.filter;

import com.example.gateway.global.error.exception.token.TokenValidException;
import com.example.gateway.global.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

import static com.example.gateway.global.constants.Address.AUTH_URL;
import static com.example.gateway.global.constants.Address.LOGIN_URL;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config>{

    private StopWatch stopWatch;
    @Autowired
    private JwtUtil jwtUtil;
    @Value("${solved.ip}")
    private String solvedIp;

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

            /**
             * ip 체크
             */
            String ip = null;
            InetSocketAddress address = request.getRemoteAddress();
            if (address != null) {
                ip = address.getAddress().getHostAddress();
            }

            // Request 요청시 최초로 실행되는 필터
//            stopWatch.start();
            log.info("[Filter] REQUEST >>> IP : {}, URI : {}", request.getRemoteAddress(), request.getURI());
            if (request.getURI().getPath().equals(AUTH_URL) || request.getURI().getPath().contains(LOGIN_URL) || ip.contains(solvedIp)) {
                return chain.filter(exchange);
            }
//             토큰 검증
            try {
                String jwtToken = request.getHeaders().getFirst("Authorization");
                if (jwtUtil.validateToken(jwtToken)) {
                    return chain.filter(exchange);
                }
            } catch (NullPointerException e) {
                throw new TokenValidException("토큰이 없습니다.");
            } catch (ExpiredJwtException e) {
                if (request.getHeaders().getFirst("refreshToken") != null) {
                    return chain.filter(exchange);
                }
            }





//            throw new TokenValidException("토큰 검증 실패");



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
