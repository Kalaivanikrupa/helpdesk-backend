package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true) // Orey email-la rendu per sign-up panna koodathu
    private String email;

    private String password;

    private String role; // USER, SUPPORT, MANAGER, ADMIN
    private String domain; // software, hardware, etc.
    private boolean available = true;
}