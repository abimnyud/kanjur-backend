package com.enjoy.kanjurbackend.user;

import com.enjoy.kanjurbackend.cart.Cart;
import com.enjoy.kanjurbackend.cart.CartRepository;
import com.enjoy.kanjurbackend.product.Product;
import com.enjoy.kanjurbackend.product.ProductRepository;
import com.enjoy.kanjurbackend.transaction.Transaction;
import com.enjoy.kanjurbackend.transaction.TransactionRepository;
import com.enjoy.kanjurbackend.user.dto.*;

// import java.security.SecureRandom;
// import java.math.BigInteger;
// import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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

    private boolean checkId(Integer id) {
        String strValue = id.toString();

        Integer sum = 0;
        Integer twoLastDigit = Integer.parseInt(strValue.substring(3, 5)); // last two index

        char[] chars = strValue.substring(0, 4).toCharArray();

        List<Character> firstThreeDigitList = new ArrayList<Character>();

        for (char c : chars) {
            firstThreeDigitList.add(c);
        }

        for (char c : firstThreeDigitList) {
            Integer n = Integer.parseInt(Character.toString(c));
            sum += n;
        }

        if (sum != twoLastDigit) throw new Error("Student ID is not valid.");
        return true;
    }

    @Override
    public User create(CreateUserDto dto) throws Error {
        User existsUser = userRepository.getById(dto.id);
        User user;

        this.checkId(dto.id);

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
        // String encodedPassword = user.getPassword();

        try {
            String encryptedPassword;

            encryptedPassword = Encryptor.encryptString(user.getPassword());

            user.setPassword(encryptedPassword);

        } catch(NoSuchAlgorithmException nsae) {
            nsae.getMessage();
        }


        /**
         * Update password with the encrypted one
         */
        // user.setPassword(encodedPassword);
        // user.setPassword(encryptedPassword);
        
        return userRepository.save(user);
    }

    @Override
    public User login(UserLoginDto dto) throws Error {
        User currentUser = userRepository.getUser(dto.id);

        try {
            String encryptedPassword;
            encryptedPassword = Encryptor.encryptString(dto.password);
            
            if (currentUser != null) {
                /**
                 * Compare password
                 */
                // BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(Common.ENCODE_STRENGTH, new SecureRandom());
                // boolean isValid = bCryptPasswordEncoder.matches(dto.password, currentUser.getPassword());
                boolean isValid = encryptedPassword.equals(currentUser.getPassword());

                if (!isValid) {
                    throw new Error("Invalid password");
                };
            };

        } catch(NoSuchAlgorithmException nsae) {
            nsae.getMessage();
        }

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