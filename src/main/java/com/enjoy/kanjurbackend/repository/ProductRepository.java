package com.enjoy.kanjurbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.enjoy.kanjurbackend.model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product AS p WHERE p.id = :productId AND p.isDeleted = false")
    Product getUser(@Param("productId") Integer productId);
}
