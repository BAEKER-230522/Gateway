package com.example.gateway.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.logout.*;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .oauth2Login(Customizer.withDefaults())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint("/login/oauth2")))
                .build();
    }

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .oauth2Login(oauth2 -> oauth2
                        .authenticationConverter(this.authenticationConverter())
                        .authenticationMatcher(this.authenticationMatcher())
                        .authenticationManager(this.authenticationManager())
                        .authenticationSuccessHandler(this.authenticationSuccessHandler())
                        .authenticationFailureHandler(this.authenticationFailureHandler())
                        .clientRegistrationRepository(this.clientRegistrationRepository())
                        .authorizedClientRepository(this.authorizedClientRepository())
                        .authorizedClientService(this.authorizedClientService())
                        .authorizationRequestResolver(this.authorizationRequestResolver())
                        .authorizationRequestRepository(this.authorizationRequestRepository())
                        .securityContextRepository(this.securityContextRepository())
                );

    @Bean
    public ReactiveOAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcReactiveOAuth2UserService delegate = new OidcReactiveOAuth2UserService();

        return (userRequest) -> {
            // Delegate to the default implementation for loading a user
            return delegate.loadUser(userRequest)
                    .flatMap((oidcUser) -> {
                        OAuth2AccessToken accessToken = userRequest.getAccessToken();
                        Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

                        // TODO
                        // 1) Fetch the authority information from the protected resource using accessToken
                        // 2) Map the authority information to one or more GrantedAuthority's and add it to mappedAuthorities
                        // 3) Create a copy of oidcUser but use the mappedAuthorities instead

                        oidcUser = new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
                        return Mono.just(oidcUser);
                    }).flatMap((oidcUser) -> {

                        // TODO
                        // 1) Save the mappedAuthorities to the database
                        // 2) Create a copy of oidcUser but use the mappedAuthorities instead

                        return Mono.just(oidcUser);
                    });
        };
    }


}
