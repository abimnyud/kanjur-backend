package com.enjoy.kanjurbackend.controller;

import com.enjoy.kanjurbackend.dto.user.CreateUserDto;
import com.enjoy.kanjurbackend.dto.user.UserLoginDto;
import com.enjoy.kanjurbackend.model.User;
import com.enjoy.kanjurbackend.service.UserService;
import com.enjoy.kanjurbackend.shared.BaseResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody CreateUserDto dto) {
        try {
            User isExists = userService.find(Integer.parseInt(dto.id));
            if (isExists != null) {
                return new ResponseEntity<BaseResponse<String>>(
                    new BaseResponse<String>(false, HttpStatus.FORBIDDEN, "User already registered."), 
                    HttpStatus.FORBIDDEN
                );
            }
        } catch (NoSuchElementException e) {}

        User createdUser = userService.create(dto);

        return new ResponseEntity<BaseResponse<User>>(
            new BaseResponse<User>(true, HttpStatus.CREATED, createdUser), 
            HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<User>> create(@RequestBody UserLoginDto dto) {
        User user = userService.login(dto);

        return new ResponseEntity<BaseResponse<User>>(
            new BaseResponse<User>(true, HttpStatus.OK, user), 
            HttpStatus.OK
        );
    }


    @GetMapping("/")
    public ResponseEntity<BaseResponse<Page<User>>> findAll(
        @RequestParam(value = "skip") Integer skip, 
        @RequestParam(value = "take") Integer take
    ) {
        if (skip == null) skip = 0;
        if (take == null) take = 10;
        
        Page<User> userData = userService.find(skip, take);

        return new ResponseEntity<BaseResponse<Page<User>>>(
            new BaseResponse<Page<User>>(true, HttpStatus.OK, userData), 
            HttpStatus.OK
        );
    }
   
    @GetMapping("/{userId}")
    public ResponseEntity<?> get(@PathVariable Integer userId) {
        try {
            User user = userService.find(userId);
            if (user == null) {
                throw new NotFoundException();
            }

            return new ResponseEntity<BaseResponse<User>>(
                new BaseResponse<User>(true, HttpStatus.OK, user), 
                HttpStatus.OK
            );
        } catch (NotFoundException e) {
            return new ResponseEntity<BaseResponse<String>>(
                new BaseResponse<String>(false, HttpStatus.NOT_FOUND, "User not found."), 
                HttpStatus.NOT_FOUND
            );
        }
    }
//
//    @PostMapping("/")
//    public void add(@RequestBody User user) {
//        userService.saveUser(user);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> update(@RequestBody User user, @PathVariable Integer id) {
//        try {
//            User existUser = userService.getUser(id);
//            user.setId(id);
//            userService.saveUser(user);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } catch (NoSuchElementException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
//
   @DeleteMapping("/{userId}")
   public ResponseEntity<BaseResponse<String>> delete(@PathVariable Integer userId) {
    try {
        userService.find(userId);
    } catch (NoSuchElementException e) {
        return new ResponseEntity<BaseResponse<String>>(
            new BaseResponse<String>(false, HttpStatus.NOT_FOUND, "User not found."), 
            HttpStatus.NOT_FOUND
        );
    }

    userService.delete(userId);
    return new ResponseEntity<BaseResponse<String>>(
        new BaseResponse<String>(false, HttpStatus.OK, "Successfully deleted user with id " + userId + "."), 
        HttpStatus.OK
    );
       
   }
}
