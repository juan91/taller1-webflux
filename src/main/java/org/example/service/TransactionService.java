package org.example.service;

import org.example.dto.CashRequestDto;
import org.example.dto.TransactionDto;
import org.example.model.Transaction;
import org.example.model.TransactionStatus;
import org.example.model.TransactionType;
import org.example.repo.TransactionRepo;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import javax.swing.text.ChangedCharSetException;
import java.time.Duration;
import java.time.Instant;

@Service
public class TransactionService {

    private final TransactionRepo transactionRepo;
    private final LedgerClient ledgerClient;

    public TransactionService(TransactionRepo transactionRepo, LedgerClient ledgerClient) {
        this.transactionRepo = transactionRepo;
        this.ledgerClient = ledgerClient;
    }

    public Mono<TransactionDto> cashIn(CashRequestDto cashRequestDto) {
        Transaction tx = Transaction.builder()
                .amount(cashRequestDto.amount())
                .currency(cashRequestDto.currency())
                .status(TransactionStatus.PENDING)
                .type(TransactionType.CASH_IN)
                .createdAt(Instant.now())
                .build();

/*        return transactionRepo.save(tx)
                .flatMap(savedTx ->
                        ledgerClient.postEntry(savedTx)
                                .retryWhen(
                                        Retry.backoff(3, Duration.ofSeconds(2))
                                                .onRetryExhaustedThrow((spec, signal) -> signal.failure())
                                )
                )
                .map(this::toDto)
                .onErrorResume(ex -> {
                    log.error("Fallo luego de reintentos", ex);
                    return Mono.error(new CustomException("No se pudo completar la operación", ex));
                });*/
        return transactionRepo.save(tx)
                .flatMap(savedTx ->
                        ledgerClient.postEntry(savedTx)
                                .retryWhen(
                                        Retry.backoff(3, Duration.ofSeconds(2))
                                                .maxBackoff(Duration.ofSeconds(10))
                                                .filter(e -> e instanceof RuntimeException)
                                                .onRetryExhaustedThrow((spec, signal) -> signal.failure())
                                )
                                .map(this::toDto)
                                .onErrorResume(e -> rollback(savedTx, e))
                );
    }

    public Mono<TransactionDto> findById(String id) {
        return transactionRepo.findById(id)
                .map(this::toDto);
              //  .switchIfEmpty(Mono.error(new ChangeSetPersister.NotFoundException()));
    }

    public Mono<TransactionDto> cashOut(CashRequestDto cashRequestDto) {
        Transaction tx = Transaction.builder()
                .amount(cashRequestDto.amount())
                .currency(cashRequestDto.currency())
                .status(TransactionStatus.PENDING)
                .type(TransactionType.CASH_OUT)
                .createdAt(Instant.now())
                .build();

        return transactionRepo.save(tx)
                .flatMap(ledgerClient::postEntry)
                .map(this::toDto)
                .retryWhen(// si falla ejecute todo el flujo incluso, el save
                        Retry.backoff(3, Duration.ofSeconds(2)) // 3 intentos, con backoff exponencial desde 2s
                                .maxBackoff(Duration.ofSeconds(10)) // Tiempo máximo entre reintentos
                                .filter(e -> e instanceof RuntimeException) // solo reintenta si es este tipo de error
                                .onRetryExhaustedThrow((spec, signal) ->
                                        signal.failure() // si se agotan los reintentos, lanza el último error
                                )

                )
                .onErrorResume(e -> rollback(tx, e));
    }

    private Mono<TransactionDto> rollback(Transaction tx, Throwable e) {
        tx.setStatus(TransactionStatus.FAILED);
        return transactionRepo.save(tx).then(Mono.error(e));
    }

    private TransactionDto toDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getCreatedAt());
    }

}