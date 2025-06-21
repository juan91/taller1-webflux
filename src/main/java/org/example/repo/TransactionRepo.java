package org.example.repo;

import org.example.model.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends ReactiveCrudRepository<Transaction, String> {
}
