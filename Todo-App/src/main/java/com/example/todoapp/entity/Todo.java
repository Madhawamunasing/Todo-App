package com.example.todoapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Todo {
    @Id
    @GeneratedValue
    private Long Id;

    @Column(nullable = false)
    private String Title;
    private String Description;

    @Enumerated(EnumType.STRING)
    private Priority Priority;

    @ManyToOne
    private User user;

    private boolean Completed = false;
}