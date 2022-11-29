package com.enjoy.kanjurbackend.product;

import org.springframework.data.domain.Page;

import com.enjoy.kanjurbackend.product.dto.CreateProductDto;
import com.enjoy.kanjurbackend.product.dto.UpdateProductDto;

public interface ProductService {
    Product create(CreateProductDto dto);
 
    Product find(Integer productId);

    Page<Product> find(Integer skip, Integer take, String keyword);
 
    Product update(Integer productId, UpdateProductDto product);
 
    boolean delete(Integer productId);
}