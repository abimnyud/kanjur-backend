package com.enjoy.kanjurbackend.cart;

import javax.persistence.*;

import com.enjoy.kanjurbackend.product.Product;

import lombok.Data;

@Entity
@Table(name = "carts")
@Data
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "qty", nullable = false)
    private Integer qty;

    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id", insertable = false, updatable = false)
    public Product product;

    public Cart() { }

    public Cart(Integer userId, Integer productId, Integer qty, Double price) {
        this.userId = userId;
        this.productId = productId;
        this.qty = qty;
        this.price = qty * price;
    }
}
