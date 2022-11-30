package com.enjoy.kanjurbackend.product;

import java.sql.Timestamp;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import com.enjoy.kanjurbackend.product.dto.CreateProductDto;
// import com.enjoy.kanjurbackend.user.User;

import lombok.Data;

@Entity
@Table(name = "products")
@Data
@SQLDelete(sql = "UPDATE products SET is_deleted = true WHERE id=? AND is_deleted = false")
@FilterDef(name = "deletedProductFilter", parameters = @ParamDef(name = "isDeleted", type = "boolean"))
@Filter(name = "deletedProductFilter", condition = "is_deleted = :isDeleted")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = Boolean.FALSE;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name="created_by", insertable = false, updatable = false)
    // public User user;

    public Product() {}

    public Product(CreateProductDto dto) {
        this.name = dto.name;
        this.image = dto.image;
        this.description = dto.description;
        this.price = dto.price;
        this.stock = dto.stock;
        this.createdBy = dto.createdBy;
    }
}
