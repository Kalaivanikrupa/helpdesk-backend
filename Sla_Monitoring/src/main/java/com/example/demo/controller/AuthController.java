package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
// originPatterns use pannuna dhaan credentials allow aagum
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public User signup(@RequestBody User user) {
        return authService.registerUser(user);
    }

    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> loginData) {
        return authService.loginUser(loginData.get("email"), loginData.get("password"));
    }
}