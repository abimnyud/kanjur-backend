package com.enjoy.kanjurbackend.transaction;

import java.sql.Timestamp;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Data;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "total_price", nullable = false)
    private double totalPrice = 0.00;

    @Column(name = "deposit", nullable = false)
    private double deposit = 0.00;
    
    @Column(name = "withdraw", nullable = false)
    private double withdraw;
    
    @Column(name = "balance", nullable = false)
    private double balance;
    
    @Column(name = "flag", nullable = false)
    private boolean flag = Boolean.FALSE;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    public Transaction() {}
}
