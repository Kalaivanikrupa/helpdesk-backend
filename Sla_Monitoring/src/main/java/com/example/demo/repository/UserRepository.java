package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Login logic-ku name illa email vachi find panna
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);

    // 1. Domain and Availability base panni support member-ah find panna (Manager Dashboard-kaga)
    List<User> findByDomainIgnoreCaseAndRoleAndAvailableTrue(String domain, String role);

    // 2. Domain base panni manager-ah find panna
    Optional<User> findByDomainAndRole(String domain, String role);

    // 3. Admin-ku domain wise users list-ah paarkka (Pudhusa add panniyachu)
    List<User> findByDomainIgnoreCase(String domain);

    // 4. Role base panni users-ah filter panna (e.g. Ella Managers mattum edukka)
    List<User> findByRole(String role);
}