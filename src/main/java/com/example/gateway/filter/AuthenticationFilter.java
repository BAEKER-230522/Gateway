package com.example.gateway.filter;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (user)
        }
    }

    public static class Config{}


    private void mutateHeader(ServerWebExchange exchange, UserInfo userInfo) {

    }
}
