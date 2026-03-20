package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔐 LOGIN
    Optional<User> findByEmailAndPasswordAndActiveTrue(String email, String password);

    // 🔍 Find by email
    Optional<User> findByEmail(String email);

    // 🔍 Check if email exists
    boolean existsByEmail(String email);

    // 📱 Find by phone
    Optional<User> findByPhone(String phone);

    // 📱 Check phone duplicate
    boolean existsByPhone(String phone);

    // ✅ Get all active users
    List<User> findByActiveTrue();

    // 🔍 Search (IMPROVED 🔥)
    List<User> findByFirstNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);
}