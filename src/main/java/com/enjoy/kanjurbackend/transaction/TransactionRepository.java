package com.enjoy.kanjurbackend.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query(
        value = "SELECT t.balance FROM Transactions t ORDER BY t.id DESC LIMIT 1",
        nativeQuery = true
    )
    String getBalance();

    Page<Transaction> findByUserId(Integer userId, Pageable pageable);
}
