package com.enjoy.kanjurbackend.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "transactions")
public class Transaction {
    private int id, studentId;
    private double totalPrice, deposit, withdraw, balance;
    private boolean flag;
    private Date createdAt;

    public Transaction() {}
}
