package com.project1.ms_yanki_service.config.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project1.ms_yanki_service.exception.BadRequestException;
import io.reactivex.rxjava3.core.Single;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Base64;

@Component
public class SecurityUtils {
    public Single<String> getUserIdFromToken(ServerWebExchange exchange) {
        String authorization = exchange.getRequest()
            .getHeaders()
            .getFirst("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return Single.error(new BadRequestException("Invalid token"));
        }

        String token = authorization.substring(7);
        String[] chunks = token.split("\\.");
        String payload = new String(Base64.getDecoder().decode(chunks[1]));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(payload);
            return Single.just(node.get("id").asText());
        } catch (JsonProcessingException e) {
            return Single.error(new BadRequestException("Invalid token"));
        }
    }
}
