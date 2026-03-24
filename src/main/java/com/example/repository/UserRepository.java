package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔐 LOGIN (fixed)
    Optional<User> findByEmailIgnoreCaseAndIsActiveTrue(String email);

    // 🔍 Find by email
    Optional<User> findByEmailIgnoreCase(String email);

    // 🔍 Check if email exists
    boolean existsByEmailIgnoreCase(String email);

    // 📱 Find by phone
    Optional<User> findByPhone(String phone);

    // 📱 Check phone duplicate
    boolean existsByPhone(String phone);

    // 🔍 Combined duplicate check
    boolean existsByEmailIgnoreCaseOrPhone(String email, String phone);

    // ✅ Get all active users (fixed)
    List<User> findByIsActiveTrue();

    // 🔍 Search
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName,
            String lastName,
            String email
    );
}