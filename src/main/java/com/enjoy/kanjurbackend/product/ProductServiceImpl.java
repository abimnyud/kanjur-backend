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
        return productRepository.save(new Product(dto));
    }

    @Override
    public Product find(Integer productId) {
        return productRepository.getProduct(productId);
    }

    @Override
    public Page<Product> find(Integer skip, Integer take, String keyword) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedUserFilter");
        filter.setParameter("isDeleted", false);

        Page<Product> productData;

        if (keyword != null) {
            productData = productRepository.findAllByNameLikeAndIsDeletedFalse(
                "%" + keyword + "%", 
                PageRequest.of(skip, take)
            );
        } else {
            productData = productRepository.findAll(PageRequest.of(skip, take));
        }

        session.disableFilter("deletedUserFilter");
        return productData;
    }

    @Override
    public Product update(Integer productId, UpdateProductDto product) {
        Product productData = productRepository.findById(productId).get();

        productData.setName(product.name);
        productData.setDescription(product.description);
        productData.setImage(product.image);
        productData.setPrice(product.price);
        productData.setPrice(product.stock);

        return productRepository.save(productData);
    }

    @Override
    public boolean delete(Integer productId) {
        productRepository.deleteById(productId);

        return true;
    }
    
}
