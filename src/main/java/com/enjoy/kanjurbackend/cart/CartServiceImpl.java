package com.enjoy.kanjurbackend.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enjoy.kanjurbackend.cart.dto.*;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    CartRepository cartRepository;

    @Override
    public Cart add(AddToCartDto dto) {
        Cart isExists = cartRepository.getByUserIdAndProductId(dto.userId, dto.productId);

        if (isExists != null) {
            isExists.setQty(isExists.getQty() + dto.qty);

            return cartRepository.save(isExists);
        }

        return cartRepository.save(new Cart(dto));
    }

    @Override
    public boolean remove(Integer cartId) {
        cartRepository.deleteById(cartId);
        return true;
    }
}
