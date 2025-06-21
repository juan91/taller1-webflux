package org.example.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TransactionRouter {

    @Bean
    public RouterFunction<ServerResponse> transactionRoutes(TransactionHandler handler) {
        return RouterFunctions
                .route()
                .POST("/api/v1/transactions/cash-in", handler::cashIn)
                .POST("/api/v1/transactions/cash-out", handler::cashOut)
                .GET("/api/v1/transactions/{id}", handler::findById)
                .build();
    }
}
