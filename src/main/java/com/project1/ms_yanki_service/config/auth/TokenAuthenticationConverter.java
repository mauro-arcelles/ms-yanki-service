package com.project1.ms_yanki_service.config.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
public class TokenAuthenticationConverter implements Function<ServerWebExchange, Mono<Authentication>> {
    @Override
    public Mono<Authentication> apply(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("Authorization"))
            .filter(authHeader -> authHeader.startsWith("Bearer "))
            .map(authHeader -> authHeader.substring(7))
            .map(token -> new UsernamePasswordAuthenticationToken(token, token));
    }
}
