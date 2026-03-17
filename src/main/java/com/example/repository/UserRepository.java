package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔍 Find by email (LOGIN)
    Optional<User> findByEmail(String email);

    // 🔍 Check if email exists (REGISTER)
    boolean existsByEmail(String email);

    // 🔍 Find by phone
    Optional<User> findByPhone(String phone);

    // 🔍 Check phone duplicate
    boolean existsByPhone(String phone);

    // 🔍 Get active users
    List<User> findByActiveTrue();

    // 🔍 Search users
    List<User> findByFirstNameContainingIgnoreCase(String keyword);
}