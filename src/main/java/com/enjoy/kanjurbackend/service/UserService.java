package com.enjoy.kanjurbackend.service;

import org.springframework.data.domain.Page;

import com.enjoy.kanjurbackend.dto.user.*;
import com.enjoy.kanjurbackend.model.User;

public interface UserService {
    User create(CreateUserDto dto);

    User login(UserLoginDto dto);
 
    User find(Integer userId);

    Page<User> find(Integer skip, Integer take);
 
    User update(Integer userId, UpdateUserDto dto);
 
    boolean delete(Integer userId);
}