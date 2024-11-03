package com.example.todoapp.service;

import com.example.todoapp.dto.LoginRequest;
import com.example.todoapp.entity.User;
import com.example.todoapp.repository.UserRepository;
import com.example.todoapp.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void Setup() {
            MockitoAnnotations.openMocks(this);
    }

    @Test
    public void TestRegisterUser() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setPassword("test345");

        String token = "mocked-jwt-token";
        when(jwtUtil.generateToken(user.getEmail())).thenReturn(token);

        String result = userService.Register(user);
        assertEquals(token, result);
    }

    @Test
    public void TestLoginUser() {
        LoginRequest request = new LoginRequest();
        request.setEmail("indduwara@gmail.com");
        request.setPassword("madumadhawa");

        String token = "mocked-jwt-token";
        when(jwtUtil.generateToken(request.getEmail())).thenReturn(token);

        String result = userService.Login(request.getEmail(),request.getPassword());
        assertEquals(token, result);
    }

}
