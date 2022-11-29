package com.enjoy.kanjurbackend.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.id = :userId AND u.isDeleted = false")
    User getUser(@Param("userId") Integer userId);

    @Query("SELECT u from User u WHERE u.id = :userId")
    User getById(@Param("userId") Integer userId);

    Page<User> findAllByNameLikeAndIsDeletedFalse(String name, Pageable pageable);
}
