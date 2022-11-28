package com.enjoy.kanjurbackend.model;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import com.enjoy.kanjurbackend.dto.user.CreateUserDto;

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
    private double revenue = 0.00;
    
    @Column(name = "deposit", nullable = false)
    private double deposit = 0.00;
    
    @Column(name = "withdraw", nullable = false)
    private double withdraw = 0.00;
    
    @Column(name = "debt", nullable = false)
    private double debt = 0.00;

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

    public User() {}

    public User(CreateUserDto dto) {
        this.id = Integer.parseInt(dto.id);
        this.password = dto.password;
        this.name = dto.name;
    }
}
