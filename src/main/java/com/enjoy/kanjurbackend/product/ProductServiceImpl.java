package com.enjoy.kanjurbackend.product;

import javax.persistence.EntityManager;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enjoy.kanjurbackend.product.dto.CreateProductDto;
import com.enjoy.kanjurbackend.product.dto.UpdateProductDto;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EntityManager entityManager;


    @Override
    public Product create(CreateProductDto dto) {
        return this.productRepository.save(new Product(dto));
    }

    @Override
    public Product find(Integer productId) {
        Product productData = this.productRepository.getProduct(productId);

        return productData;
    }

    @Override
    public Page<Product> find(Integer skip, Integer take, String keyword) {
        Session session = this.entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedUserFilter");
        filter.setParameter("isDeleted", false);

        Page<Product> productData;

        if (keyword != null) {
            productData = this.productRepository.findAllByNameLikeAndIsDeletedFalse(
                "%" + keyword + "%", 
                PageRequest.of(skip, take)
            );
        } else {
            productData = this.productRepository.findAll(PageRequest.of(skip, take));
        }

        session.disableFilter("deletedUserFilter");
        return productData;
    }

    @Override
    public Product update(Integer productId, UpdateProductDto product) {
        Product productData = this.find(productId);

        productData.setName(product.name);
        productData.setDescription(product.description);
        productData.setImage(product.image);
        productData.setPrice(product.price);
        productData.setStock(product.stock);

        return productData;
    }

    @Override
    public boolean delete(Integer productId) {
        this.productRepository.deleteById(productId);

        return true;
    }
    
}
