package com.enjoy.kanjurbackend.cart;

import com.enjoy.kanjurbackend.cart.dto.*;

public interface CartService {
    Cart add(AddToCartDto dto);
    
    boolean remove(Integer cartId);
}
