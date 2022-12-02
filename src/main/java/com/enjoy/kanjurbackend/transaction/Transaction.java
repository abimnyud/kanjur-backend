package com.enjoy.kanjurbackend.transaction;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.enjoy.kanjurbackend.product.Product;
// import com.enjoy.kanjurbackend.user.User;

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
    private Double totalPrice = 0.00;

    @Column(name = "deposit", nullable = false)
    private Double deposit = 0.00;
    
    @Column(name = "withdraw", nullable = false)
    private Double withdraw = 0.00;
    
    @Column(name = "balance", nullable = false)
    private Double balance;
    
    @Column(name = "flag", nullable = false)
    private boolean flag = Boolean.FALSE;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "product_transaction",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "transaction_id")
    )
    Set<Product> items;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name="user_id", nullable=false, insertable = false, updatable = false)
    // private User user;

    public Transaction() {}
}
