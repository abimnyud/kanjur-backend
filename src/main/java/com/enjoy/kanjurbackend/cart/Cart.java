package com.enjoy.kanjurbackend.cart;

import javax.persistence.*;

import com.enjoy.kanjurbackend.cart.dto.AddToCartDto;

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
    private double price;

    public Cart() { }

    public Cart(AddToCartDto dto) {
        this.userId = dto.userId;
        this.productId = dto.productId;
        this.qty = dto.qty;
        this.price = dto.price;
    }
}
