package com.enjoy.kanjurbackend.repository;
import com.enjoy.kanjurbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User AS u WHERE u.id = :userId AND u.isDeleted = false")
    User getUser(@Param("userId") Integer userId);
}
