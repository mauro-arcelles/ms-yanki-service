package com.project1.ms_yanki_service.config;

import com.project1.ms_yanki_service.config.auth.TokenAuthenticationConverter;
import com.project1.ms_yanki_service.config.auth.jwt.JWTReactiveAuthenticationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Autowired
    private JWTReactiveAuthenticationManager jwtReactiveAuthenticationManager;

    @Autowired
    private TokenAuthenticationConverter tokenAuthenticationConverter;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .logout().disable()
            .authorizeExchange()
            .pathMatchers(HttpMethod.OPTIONS).permitAll()
            .anyExchange().authenticated()
            .and()
            .addFilterAt(webFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
            .build();
    }

    @Bean
    public AuthenticationWebFilter webFilter() {
        AuthenticationWebFilter filter = new AuthenticationWebFilter(jwtReactiveAuthenticationManager);
        filter.setAuthenticationConverter(tokenAuthenticationConverter);
        filter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        return filter;
    }
}
