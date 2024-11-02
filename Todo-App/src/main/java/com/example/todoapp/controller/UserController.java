package com.example.todoapp.controller;

import com.example.todoapp.dto.LoginRequest;
import com.example.todoapp.entity.User;
import com.example.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> Register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            String token = userService.Register(user);
            response.put("message", "User registered successfully!");
            response.put("token", token);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("message", "This email address is already in use. Please choose a different one.");
            response.put("token", null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> Login(@RequestBody LoginRequest loginRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            String token = userService.Login(loginRequest.getEmail(),loginRequest.getPassword());
            response.put("message", "Login successfull!");
            response.put("token", token);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            response.put("token", null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}
