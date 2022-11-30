package com.enjoy.kanjurbackend.user;

import com.enjoy.kanjurbackend.cart.Cart;
import com.enjoy.kanjurbackend.cart.CartRepository;
import com.enjoy.kanjurbackend.product.Product;
import com.enjoy.kanjurbackend.product.ProductRepository;
import com.enjoy.kanjurbackend.transaction.Transaction;
import com.enjoy.kanjurbackend.transaction.TransactionRepository;
import com.enjoy.kanjurbackend.user.dto.*;

// import java.security.SecureRandom;

import javax.persistence.EntityManager;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public User create(CreateUserDto dto) {
        User existsUser = userRepository.getById(dto.id);
        User user;

        if (existsUser != null && existsUser.isDeleted() == true) {
            existsUser.setDeleted(false);
            user = existsUser;
        } else {
            user = new User(dto);
        }

        /**
         * Encrypt password
         */
        // BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(Common.ENCODE_STRENGTH, new SecureRandom());
        // String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        String encodedPassword = user.getPassword();

        /**
         * Update password with the encrypted one
         */
        user.setPassword(encodedPassword);
        
        return userRepository.save(user);
    }

    @Override
    public User login(UserLoginDto dto) throws Error {
        User currentUser = userRepository.getUser(dto.id);

        if (currentUser != null) {
            /**
             * Compare password
             */
            // BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(Common.ENCODE_STRENGTH, new SecureRandom());
            // boolean isValid = bCryptPasswordEncoder.matches(dto.password, currentUser.getPassword());
            boolean isValid = currentUser.getPassword().equals(dto.password);

            if (!isValid) {
                throw new Error("Invalid password");
            };
        };

        return currentUser;
    }

    @Override
    public User find(Integer userId) {
        return userRepository.getUser(userId);
    }

    @Override
    public Page<User> find(Integer skip, Integer take, String keyword) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedUserFilter");
        filter.setParameter("isDeleted", false);

        Page<User> userData;
        if (keyword != null) {
            userData = userRepository.findAllByNameLikeAndIsDeletedFalse(
                "%" + keyword + "%", 
                PageRequest.of(skip, take)
            );
        } else {
            userData = userRepository.findAll(PageRequest.of(skip, take));
        }

        session.disableFilter("deletedUserFilter");
        return userData;
    }

    @Override
    public Page<Product> getUserProduct(Integer userId, Integer skip, Integer take) {
        return productRepository.findByCreatedBy(userId, PageRequest.of(skip, take));
    }

    @Override
    public Page<Cart> getUserCart(Integer userId, Integer skip, Integer take) {
        return cartRepository.findByUserId(userId, PageRequest.of(skip, take));
    }

    @Override
    public Page<Transaction> getUserTransaction(Integer userId, Integer skip, Integer take) {
        return transactionRepository.findByUserId(userId, PageRequest.of(skip, take));
    }

    @Override
    public User update(Integer userId, UpdateUserDto user) {
        User userData = userRepository.findById(userId).get();

        userData.setName(user.name);
        userData.setPassword(user.password);

        return userRepository.save(userData);
    };

    @Override
    public boolean delete(Integer userId) {
        User existsUser = userRepository.getById(userId);
        if (existsUser != null && existsUser.isDeleted() == true) {
            return false;
        }

        userRepository.deleteById(userId);

        return true;
    }
}