package com.enjoy.kanjurbackend.user;

import com.enjoy.kanjurbackend.cart.Cart;
import com.enjoy.kanjurbackend.product.Product;
import com.enjoy.kanjurbackend.shared.BaseResponse;
import com.enjoy.kanjurbackend.transaction.Transaction;
import com.enjoy.kanjurbackend.user.dto.CreateUserDto;
import com.enjoy.kanjurbackend.user.dto.UpdateUserDto;
import com.enjoy.kanjurbackend.user.dto.UserLoginDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Tag(name = "user")
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
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
    @Operation(summary = "Login with existing user")
    public ResponseEntity<?> create(@RequestBody UserLoginDto dto) {
        User user;
        try {
            user = userService.login(dto);
        } catch (Throwable t) {
            return new ResponseEntity<BaseResponse<String>>(
                new BaseResponse<String>(false, HttpStatus.FORBIDDEN, t.getMessage()), 
                HttpStatus.FORBIDDEN
            );
        }

        return new ResponseEntity<BaseResponse<User>>(
            new BaseResponse<User>(true, HttpStatus.OK, user), 
            HttpStatus.OK
        );
    }

    @GetMapping("")
    @Operation(summary = "Get list of users")
    public ResponseEntity<BaseResponse<Page<User>>> findAll(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "skip") Integer skip, 
        @RequestParam(value = "take") Integer take
    ) {
        if (skip == null) skip = 0;
        if (take == null) take = 10;
        
        Page<User> userData = userService.find(skip, take, keyword);

        return new ResponseEntity<BaseResponse<Page<User>>>(
            new BaseResponse<Page<User>>(true, HttpStatus.OK, userData), 
            HttpStatus.OK
        );
    }
   
    @GetMapping("/{userId}")
    @Operation(summary = "Get user by Id")
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

    @GetMapping("/{userId}/products")
    @Operation(summary = "Get user's product by User Id")
    public ResponseEntity<?> getUserProduct(
        @PathVariable Integer userId,
        @RequestParam("skip") Integer skip,
        @RequestParam("take") Integer take
    ) {
        return new ResponseEntity<BaseResponse<Page<Product>>>(
            new BaseResponse<Page<Product>>(
                true, 
                HttpStatus.OK, 
                userService.getUserProduct(userId, skip, take)
            ), 
            HttpStatus.OK
        );
    }

    @GetMapping("/{userId}/cart")
    @Operation(summary = "Get user's cart by User Id")
    public ResponseEntity<?> getUserCart(
        @PathVariable Integer userId,
        @RequestParam("skip") Integer skip,
        @RequestParam("take") Integer take
    ) {
        return new ResponseEntity<BaseResponse<Page<Cart>>>(
            new BaseResponse<Page<Cart>>(
                true, 
                HttpStatus.OK, 
                userService.getUserCart(userId, skip, take)
            ), 
            HttpStatus.OK
        );
    }

    @GetMapping("/{userId}/transactions")
    @Operation(summary = "Get user's transaction by User Id")
    public ResponseEntity<?> getUserTransaction(
        @PathVariable Integer userId,
        @RequestParam("skip") Integer skip,
        @RequestParam("take") Integer take
    ) {
        return new ResponseEntity<BaseResponse<Page<Transaction>>>(
            new BaseResponse<Page<Transaction>>(
                true, 
                HttpStatus.OK, 
                userService.getUserTransaction(userId, skip, take)
            ), 
            HttpStatus.OK
        );
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update user")
    public ResponseEntity<?> update(
        @RequestBody UpdateUserDto user, 
        @PathVariable Integer userId
    ) {
        try {
            User userData = userService.find(userId);

            // TODO: Update user data (name and password only)

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete user by Id")
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable Integer userId) {
        try {
            userService.find(userId);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<BaseResponse<String>>(
                new BaseResponse<String>(false, HttpStatus.NOT_FOUND, "User not found."), 
                HttpStatus.NOT_FOUND
            );
        }

        boolean isSuccess = userService.delete(userId);
        if (!isSuccess) {
            return new ResponseEntity<BaseResponse<String>>(
                new BaseResponse<String>(false, HttpStatus.FORBIDDEN, "User with id " + userId + " has already been deleted"), 
                HttpStatus.FORBIDDEN
            );
        }

        return new ResponseEntity<BaseResponse<String>>(
            new BaseResponse<String>(false, HttpStatus.OK, "Successfully delete user with id " + userId + "."), 
            HttpStatus.OK
        );   
    }
}
