package com.enjoy.kanjurbackend.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cart")
public class Cart {
    private int studentId, productId;

    public Cart() {}
}
