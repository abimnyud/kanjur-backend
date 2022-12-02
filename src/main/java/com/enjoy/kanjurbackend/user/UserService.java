package com.enjoy.kanjurbackend.user;

import org.springframework.data.domain.Page;

import com.enjoy.kanjurbackend.cart.Cart;
import com.enjoy.kanjurbackend.product.Product;
import com.enjoy.kanjurbackend.transaction.Transaction;
import com.enjoy.kanjurbackend.user.dto.*;

public interface UserService {
    User create(CreateUserDto dto);

    User login(UserLoginDto dto);
 
    User find(Integer userId);

    Page<User> find(Integer skip, Integer take, String keyword);

    Page<Product> getUserProduct(Integer userId, Integer skip, Integer take);

    Page<Cart> getUserCart(Integer userId, Integer skip, Integer take);

    Page<Transaction> getUserTransaction(Integer userId, Integer skip, Integer take);
 
    User update(Integer userId, UpdateUserDto dto);
 
    /**
     * Delete User By User Id
     * 
     * @param userId userId to delete
     * @return boolean; <strong>true</strong> if delete success, <strong>false</strong> if has already been deleted
     */
    boolean delete(Integer userId);
}