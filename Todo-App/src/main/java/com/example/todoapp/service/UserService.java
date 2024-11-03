package com.example.todoapp.service;

import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import com.example.todoapp.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public String Register(User user) {
        userRepository.save(user);
        return jwtUtil.generateToken(user.getEmail());
    }

    public String Login(String email,String password) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Invalid Email. Please check the email."));
        if (!new BCryptPasswordEncoder().matches(password,user.getPassword())) {
            throw new RuntimeException("Invalid Password. Please check the password.");
        }
        return jwtUtil.generateToken(user.getEmail());
    }
}
