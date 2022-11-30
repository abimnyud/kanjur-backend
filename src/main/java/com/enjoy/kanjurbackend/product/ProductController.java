package com.enjoy.kanjurbackend.product;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.enjoy.kanjurbackend.cart.Cart;
import com.enjoy.kanjurbackend.cart.CartService;
import com.enjoy.kanjurbackend.product.dto.CreateProductDto;
import com.enjoy.kanjurbackend.product.dto.UpdateProductDto;
import com.enjoy.kanjurbackend.shared.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "product")
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @PostMapping("")
    @Operation(summary = "Create a new product")
    public ResponseEntity<BaseResponse<Product>> create(
        @RequestBody CreateProductDto dto
    ) {
        Product createdProduct = this.productService.create(dto);

        return new ResponseEntity<BaseResponse<Product>>(
            new BaseResponse<Product>(true, HttpStatus.CREATED, createdProduct), 
            HttpStatus.CREATED
        );
    }

    @GetMapping("/{productId}")
    @Operation(summary = "Get product by Id")
    public ResponseEntity<?> findAll(
        @PathVariable Integer productId
    ) {        
        Product productData = this.productService.find(productId);

        if (productData == null) {
            return new ResponseEntity<BaseResponse<String>>(
                new BaseResponse<String>(false, HttpStatus.NOT_FOUND, "Product not found."), 
                HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<BaseResponse<Product>>(
            new BaseResponse<Product>(true, HttpStatus.OK, productData), 
            HttpStatus.OK
        );
    }

    @PostMapping("/{productId}/addToCart")
    @Operation(summary = "Add product to user's cart")
    public ResponseEntity<?> addToCart(
        @PathVariable Integer productId,
        @RequestParam(name = "userId") Integer userId,
        @RequestParam(name = "qty") Integer qty
    ) {
        Product productData = this.productService.find(productId);
        if (productData == null) {
            return new ResponseEntity<BaseResponse<String>>(
                new BaseResponse<String>(false, HttpStatus.NOT_FOUND, "Product not found."), 
                HttpStatus.NOT_FOUND
            );
        }

        if (qty > productData.getStock()) {
            return new ResponseEntity<BaseResponse<String>>(
                new BaseResponse<String>(
                    false, 
                    HttpStatus.FORBIDDEN, 
                    "Product doesn't have enough stock. (" + productData.getStock() + ")"
                ), 
                HttpStatus.FORBIDDEN
            );
        }

        Cart cart = this.cartService.add(new Cart(
            userId,
            productData.getId(),
            qty,
            productData.getPrice()
        ));

        return new ResponseEntity<BaseResponse<Cart>>(
            new BaseResponse<Cart>(true, HttpStatus.CREATED, cart), 
            HttpStatus.CREATED
        );
    }

    @GetMapping("")
    @Operation(summary = "Get list of products")
    public ResponseEntity<BaseResponse<Page<Product>>> findAll(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "skip", defaultValue = "0") Integer skip, 
        @RequestParam(value = "take", defaultValue = "10") Integer take
    ) {
        if (skip == null) skip = 0;
        if (take == null) take = 10;
        
        Page<Product> productData = this.productService.find(skip, take, keyword);

        return new ResponseEntity<BaseResponse<Page<Product>>>(
            new BaseResponse<Page<Product>>(true, HttpStatus.OK, productData), 
            HttpStatus.OK
        );
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update product")
    public ResponseEntity<?> update(
        @RequestBody UpdateProductDto dto, 
        @PathVariable Integer productId
    ) {
        Product updatedProduct = this.productService.update(productId, dto);

        return new ResponseEntity<BaseResponse<Product>>(
            new BaseResponse<Product>(
                true, 
                HttpStatus.OK, 
                updatedProduct
            ), 
            HttpStatus.OK
        );
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete product by Id")
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable Integer productId) {
        try {
            this.productService.find(productId);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<BaseResponse<String>>(
                new BaseResponse<String>(false, HttpStatus.NOT_FOUND, "Product not found."), 
                HttpStatus.NOT_FOUND
            );
        }

        boolean isSuccess = this.productService.delete(productId);
        if (!isSuccess) {
            return new ResponseEntity<BaseResponse<String>>(
                new BaseResponse<String>(false, HttpStatus.FORBIDDEN, "Product with id " + productId + " has already been deleted"), 
                HttpStatus.FORBIDDEN
            );
        }

        return new ResponseEntity<BaseResponse<String>>(
            new BaseResponse<String>(false, HttpStatus.OK, "Successfully delete product with id " + productId + "."), 
            HttpStatus.OK
        );   
    }
}
