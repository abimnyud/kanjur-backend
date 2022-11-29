package com.enjoy.kanjurbackend.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product AS p WHERE p.id = :productId AND p.isDeleted = false")
    Product getProduct(@Param("productId") Integer productId);

    Page<Product> findAllByNameLikeAndIsDeletedFalse(String name, Pageable pageable);

    Page<Product> findByCreatedBy(Integer createdBy, Pageable pageable);
}
