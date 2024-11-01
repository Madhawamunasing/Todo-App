package com.example.todoapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    private String fname;
    private String lname;

    private String password;

    @PrePersist
    private void encryptPassword() {
        this.password = new BCryptPasswordEncoder().encode(this.password);
    }

}
