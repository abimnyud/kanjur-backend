package com.enjoy.kanjurbackend.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;

    @Override
    public Cart add(Cart cart) {
        Cart isExists = cartRepository.getByUserIdAndProductId(cart.getUserId(), cart.getProductId());

        if (isExists != null) {
            isExists.setQty(isExists.getQty() + cart.getQty());

            return cartRepository.save(isExists);
        }

        return cartRepository.save(cart);
    }

    @Override
    public boolean remove(Integer cartId) {
        cartRepository.deleteById(cartId);
        return true;
    }
}
