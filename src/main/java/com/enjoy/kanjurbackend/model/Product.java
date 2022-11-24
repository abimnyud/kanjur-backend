package com.enjoy.kanjurbackend.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "products")
public class Product {
    private int id;
    private String name, image, description;
    private double price;
    private int sellerId;
    private int transactionId;
    private Date createdAt, updatedAt, deletedAt;

    public Product() {}
}
