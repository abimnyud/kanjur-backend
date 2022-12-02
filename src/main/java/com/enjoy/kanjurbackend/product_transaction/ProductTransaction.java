package com.enjoy.kanjurbackend.product_transaction;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "product_transaction")
@Data
public class ProductTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    
    @Column(name = "transaction_id", nullable = false)
    private Integer transactionId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "price", nullable = false)
    private Double price;

    public ProductTransaction() {}
}
