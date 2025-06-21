package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;



@Configuration
public class WebClientConfig {

    @Bean
    WebClient ledgerWebClient(WebClient.Builder builder, @Value("${ledger.base-url}") String urlBase) {
        return builder
                .baseUrl(urlBase)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

}