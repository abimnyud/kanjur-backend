package com.enjoy.kanjurbackend.transaction;

import org.springframework.data.domain.Page;

public interface TransactionService {
    Page<Transaction> find(Integer skip, Integer take);

    String getBalance();

    boolean deposit(Integer userId, double amount);

    boolean withdraw(Integer userId, double amount);
}
