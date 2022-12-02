package com.enjoy.kanjurbackend.user;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import com.enjoy.kanjurbackend.user.dto.CreateUserDto;

import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "users")
@Data
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id=? AND is_deleted = false")
@FilterDef(name = "deletedUserFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedUserFilter", condition = "is_deleted = :isDeleted")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "revenue", nullable = false)
    private Double revenue = 0.00;
    
    @Column(name = "deposit", nullable = false)
    private Double deposit = 0.00;
    
    @Column(name = "withdraw", nullable = false)
    private Double withdraw = 0.00;
    
    @Column(name = "debt", nullable = false)
    private Double debt = 0.00;

    @Column(name = "flag", nullable = false)
    private boolean flag = Boolean.FALSE;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    // @ManyToMany(fetch = FetchType.LAZY)
    // @JoinTable(
    //     name = "carts",
    //     joinColumns = @JoinColumn(name = "product_id"),
    //     inverseJoinColumns = @JoinColumn(name = "user_id")
    // )
    // Set<Product> cartItems;

    // @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    // Set<Transaction> transactions;


    public User() {}

    public User(CreateUserDto dto) {
        this.id = dto.id;
        this.password = dto.password;
        this.name = dto.name;
    }
}
