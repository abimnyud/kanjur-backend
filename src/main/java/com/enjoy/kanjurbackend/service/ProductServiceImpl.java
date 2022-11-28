package com.enjoy.kanjurbackend.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enjoy.kanjurbackend.dto.product.CreateProductDto;
import com.enjoy.kanjurbackend.model.Product;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Override
    public Product create(CreateProductDto dto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Product find(Integer productId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Page<Product> find(Integer skip, Integer take) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Product update(Integer productId, Product product) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean delete(Integer productId) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
