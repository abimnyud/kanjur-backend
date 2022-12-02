package com.enjoy.kanjurbackend.transaction;

import org.springframework.data.domain.Page;

public interface TransactionService {
    Page<Transaction> find(Integer skip, Integer take);

    Double getBalance();

    boolean deposit(Integer userId, Double amount);

    boolean withdraw(Integer userId, Double amount);

    boolean addUserRevenue(Integer userId, Double amount);

    boolean checkout(Integer userId, Double deposit);
}
