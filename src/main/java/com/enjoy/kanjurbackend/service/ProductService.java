package com.enjoy.kanjurbackend.service;

import org.springframework.data.domain.Page;

import com.enjoy.kanjurbackend.dto.product.CreateProductDto;
import com.enjoy.kanjurbackend.model.Product;

public interface ProductService {
    Product create(CreateProductDto dto);
 
    Product find(Integer productId);

    Page<Product> find(Integer skip, Integer take);
 
    Product update(Integer productId, Product product);
 
    boolean delete(Integer productId);
}