package com.enjoy.kanjurbackend.service;

import com.enjoy.kanjurbackend.dto.user.CreateUserDto;
import com.enjoy.kanjurbackend.dto.user.UpdateUserDto;
import com.enjoy.kanjurbackend.dto.user.UserLoginDto;
import com.enjoy.kanjurbackend.model.User;
import com.enjoy.kanjurbackend.repository.UserRepository;

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
    private EntityManager entityManager;

    @Override
    public User create(CreateUserDto dto) {
        User existsUser = userRepository.findById(Integer.parseInt(dto.id)).get();
        User user;

        if (existsUser.isDeleted() == true) {
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
    public User login(UserLoginDto dto) {
        User currentUser = userRepository.getUser(Integer.parseInt(dto.id));

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
    public Page<User> find(Integer skip, Integer take) {
        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedUserFilter");
        filter.setParameter("isDeleted", false);

        Page<User> userData = userRepository.findAll(PageRequest.of(skip, take));

        session.disableFilter("deletedUserFilter");
        return userData;
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
        userRepository.deleteById(userId);

        return true;
    }
}