package com.enjoy.kanjurbackend.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    private String name;
    private double revenue, deposit, withdraw, debt;
    private boolean flag;
    private Date createdAt, updatedAt, deletedAt;

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public User() {}

    public User(Long studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public Long getStudentId() {
        return studentId;
    }
}
