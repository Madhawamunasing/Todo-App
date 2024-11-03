package com.example.todoapp.repository;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TodoRepository extends JpaRepository<Todo,Long> {
    Page<Todo> findByUser(User user, Pageable pageable);

    @Query("SELECT t FROM Todo t WHERE (t.title LIKE %:keyword% OR t.description LIKE %:keyword%) AND t.user = :user")
    Page<Todo> SearchByKeyword(String keyword, User user, Pageable pageable);

    Page<Todo> findByUserAndCompleted(User user, Boolean completed, Pageable pageable);
}
