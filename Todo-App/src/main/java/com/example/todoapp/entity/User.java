package com.example.todoapp.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(nullable = false, unique = true)
    private String Email;
    private String Fname;
    private String Lname;

    private String Password;

    @PrePersist
    private void encryptPassword() {
        this.Password = new BCryptPasswordEncoder().encode(this.Password);
    }

}
