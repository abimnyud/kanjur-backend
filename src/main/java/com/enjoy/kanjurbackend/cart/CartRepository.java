package com.enjoy.kanjurbackend.cart;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Page<Cart> findByUserId(Integer userId, Pageable pageable);

    Cart getByUserIdAndProductId(Integer userId, Integer productId);
}
