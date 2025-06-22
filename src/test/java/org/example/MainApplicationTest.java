package org.example;

import org.example.dto.CashRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MainApplicationTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void CashIn(){
        var dto = new CashRequestDto(BigDecimal.valueOf(500000), "COP", "123");
        webTestClient.post().uri("/api/v1/transactions/cash-in")
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.status").isEqualTo("PENDING");

    }

}