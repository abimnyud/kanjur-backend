package com.enjoy.kanjurbackend.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "users")
public class User {
    private int studentId;
    private String name;
    private double revenue, deposit, withdraw, debt;
    private boolean flag;
    private Date createdAt, updatedAt, deletedAt;

    public User() {}

    public User(int studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public int getStudentId() {
        return studentId;
    }
}
