package org.example.handler;

import com.mongodb.internal.connection.Server;
import org.example.model.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class LedgerRouterHandler {

    @Bean
    public RouterFunction<ServerResponse> router(){
        return RouterFunctions.route(POST("/ledger/entries"), this::createEntry);
    }

    private  Mono<ServerResponse> createEntry(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Transaction.class)
                .flatMap(body -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", "POSTED");
                    result.put("id", UUID.randomUUID().toString());
                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(body);
                });
    }
}
