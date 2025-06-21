package org.example.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("transactions")
public class Transaction {

    @Id
    private String id;
    @Positive
    private BigDecimal amount;
    @NotBlank
    private String currency;
    private TransactionType type;
    private TransactionStatus status;
    private Instant createdAt;
}