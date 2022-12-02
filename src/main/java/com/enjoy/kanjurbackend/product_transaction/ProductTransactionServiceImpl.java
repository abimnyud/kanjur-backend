package com.enjoy.kanjurbackend.product_transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductTransactionServiceImpl implements ProductTransactionService {
    @Autowired
    ProductTransactionRepository productTransactionRepository;

    @Override
    public ProductTransaction create(ProductTransaction productTransaction) {
        return this.productTransactionRepository.save(productTransaction);
    }
    
}
