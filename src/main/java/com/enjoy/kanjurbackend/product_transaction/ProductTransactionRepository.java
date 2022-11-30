package com.enjoy.kanjurbackend.product_transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTransactionRepository extends JpaRepository<ProductTransaction, Integer> {
    List<ProductTransaction> findAllByTransactionId(Integer userId);
}
