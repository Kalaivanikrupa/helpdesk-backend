package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // 1. All Users (Admin-ku moththa list-um paarkka)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 2. Manager dashboard-la transfer panna yaralam available-nu paarkka
    @GetMapping("/available-support/{domain}")
    public List<User> getAvailableSupport(@PathVariable String domain) {
        // Domain filter panni available-ah irukkura SUPPORT team mattum edukkirom
        return userRepository.findByDomainIgnoreCaseAndRoleAndAvailableTrue(domain, "SUPPORT");
    }

    // 3. Domain based users (Domain wise count check panna thevai padum)
    @GetMapping("/domain/{domain}")
    public List<User> getUsersByDomain(@PathVariable String domain) {
        return userRepository.findByDomainIgnoreCase(domain);
    }
}