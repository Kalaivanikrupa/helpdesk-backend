package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/available-support/{domain}")
    public List<User> getAvailableSupport(@PathVariable String domain) {
        return userRepository.findByDomainIgnoreCaseAndRoleAndAvailableTrue(domain, "SUPPORT");
    }

    @GetMapping("/domain/{domain}")
    public List<User> getUsersByDomain(@PathVariable String domain) {
        return userRepository.findByDomainIgnoreCase(domain);
    }
}