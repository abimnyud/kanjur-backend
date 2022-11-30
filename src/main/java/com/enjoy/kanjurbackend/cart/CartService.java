package com.enjoy.kanjurbackend.cart;

public interface CartService {
    Cart add(Cart cart);
    
    boolean remove(Integer cartId);
}
