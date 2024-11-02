package com.example.todoapp.service;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public Todo CreateTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Page<Todo> GetTodos(User user, Pageable pageable) {
        return todoRepository.findByUser(user, pageable);
    }

    public Todo GetTodoById(Long id,User user){
        Todo todo = todoRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found."));
        if (!todo.getUser().getId().equals(user.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Access denied: You can only access todos that are belong to you.");
        }
        return todo;
    }

    public Todo UpdateTodo(Long id, Todo data, User user) {
        Todo todo = GetTodoById(id,user);
        todo.setTitle(data.getTitle());
        todo.setDescription(data.getDescription());
        todo.setDueDate(data.getDueDate());
        todo.setPriority(data.getPriority());
        todo.setCompleted(data.getCompleted());
        return todoRepository.save(todo);
    }

    public void DeleteTodo(Long id, User user) {
        Todo todo = GetTodoById(id, user);
        todoRepository.delete(todo);
    }
}
