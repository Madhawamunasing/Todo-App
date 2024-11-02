package com.example.todoapp.controller;

import com.example.todoapp.dto.LoginRequest;
import com.example.todoapp.entity.User;
import com.example.todoapp.service.UserService;
import com.example.todoapp.util.UserResponse;
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
    public ResponseEntity<UserResponse> Register(@RequestBody User user) {
        try {
            String token = userService.Register(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponse("User registered successfully!",token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new UserResponse("This email address is already in use. Please choose a different one.",null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> Login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = userService.Login(loginRequest.getEmail(),loginRequest.getPassword());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(new UserResponse("Login successfull!", token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new UserResponse(e.getMessage(),null));
        }
    }
}
