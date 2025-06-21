package org.example.config;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class GlobalErrorHandler implements ErrorWebExceptionHandler {
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return null;
    }
    // manejo de errores global
}