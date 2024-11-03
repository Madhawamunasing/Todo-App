package com.example.todoapp.controller;

import com.example.todoapp.entity.Todo;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import com.example.todoapp.service.TodoService;
import com.example.todoapp.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    private TodoService todoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public Todo CreateTodo(@RequestBody Todo todo, HttpServletRequest request) {
        User user = getAuthenticatedUser(request);
        todo.setUser(user);
        return todoService.CreateTodo(todo);
    }

    @GetMapping("/getall")
    public Page<Todo> GetTodos(HttpServletRequest request, Pageable pageable) {
        User user = getAuthenticatedUser(request);
        return todoService.GetTodos(user,pageable);
    }

    @GetMapping("/getone/{id}")
    public Todo GetTodoById(@PathVariable Long id, HttpServletRequest request) {
        User user = getAuthenticatedUser(request);
        return todoService.GetTodoById(id,user);
    }

    @GetMapping("/search")
    public Page<Todo> searchTodos(@RequestParam String keyword, HttpServletRequest request, Pageable pageable) {
        User user = getAuthenticatedUser(request);
        return todoService.SearchTodos(keyword, user, pageable);
    }

    @GetMapping("/getcompleted")
    public Page<Todo> getTodosByCompletionStatus(@RequestParam Boolean completed, HttpServletRequest request, Pageable pageable) {
        User user = getAuthenticatedUser(request);
        return todoService.GetTodosByCompleteStatus(user, completed, pageable);
    }

    @PutMapping("updatestatus/{id}")
    public ResponseEntity<String> updateCompletionStatus(@PathVariable Long id, @RequestParam Boolean completed, HttpServletRequest request) {
        User user = getAuthenticatedUser(request);
        todoService.UpdateCompleteStatus(id, completed, user);
        return ResponseEntity.ok("Todo completion status updated successfully!");
    }

    @PutMapping("/update/{id}")
    public Todo UpdateTodoById(@PathVariable Long id,@RequestBody Todo data, HttpServletRequest request) {
        User user = getAuthenticatedUser(request);
        return todoService.UpdateTodo(id,data,user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String,String>> DeleteTodo(@PathVariable Long id, HttpServletRequest request) {
        User user = getAuthenticatedUser(request);
        todoService.DeleteTodo(id,user);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Todo deleted successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    private User getAuthenticatedUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header missing or invalid");
        }

        String token = authorizationHeader.substring(7);
        String email = jwtUtil.getEmailFromToken(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not exist with this email: " + email));
    }
}
