package org.example.service;

import org.example.model.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class LedgerClient {
    // WebClient para llamar al ledger
    private final WebClient webClient;

    public LedgerClient(WebClient webClient) {
        this.webClient = webClient;
    }


    public Mono<Transaction> postEntry(Transaction transaction) {
        System.out.println("post "+transaction);
        return webClient.post().uri("/ledger/entries")
                .bodyValue(transaction)
                .retrieve()
                .bodyToMono(Transaction.class)
                .doOnNext(resp -> System.out.println("response service: "+resp))
                .doOnError(e -> System.out.println("response service: "+e));
    }
}