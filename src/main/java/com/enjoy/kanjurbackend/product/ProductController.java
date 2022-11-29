package com.enjoy.kanjurbackend.product;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("")
    @Operation(summary = "Create a new product")
    public ResponseEntity<BaseResponse<Product>> create(
        @RequestBody CreateProductDto dto
    ) {
        Product createdProduct = productService.create(dto);

        return new ResponseEntity<BaseResponse<Product>>(
            new BaseResponse<Product>(true, HttpStatus.CREATED, createdProduct), 
            HttpStatus.CREATED
        );
    }

    @GetMapping("")
    @Operation(summary = "Get list of products")
    public ResponseEntity<BaseResponse<Page<Product>>> findAll(
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(value = "skip") Integer skip, 
        @RequestParam(value = "take") Integer take
    ) {
        if (skip == null) skip = 0;
        if (take == null) take = 10;
        
        Page<Product> productData = productService.find(skip, take, keyword);

        return new ResponseEntity<BaseResponse<Page<Product>>>(
            new BaseResponse<Page<Product>>(true, HttpStatus.OK, productData), 
            HttpStatus.OK
        );
    }

    @PutMapping("/{productId}")
    @Operation(summary = "Update product")
    public ResponseEntity<?> update(
        @RequestBody UpdateProductDto product, 
        @PathVariable Integer productId
    ) {
        try {
            Product productData = productService.find(productId);
        
            // TODO: Update product data (name, image, description, price, stock)
        
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete product by Id")
    public ResponseEntity<BaseResponse<String>> delete(@PathVariable Integer productId) {
        try {
            productService.find(productId);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<BaseResponse<String>>(
                new BaseResponse<String>(false, HttpStatus.NOT_FOUND, "Product not found."), 
                HttpStatus.NOT_FOUND
            );
        }

        boolean isSuccess = productService.delete(productId);
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
