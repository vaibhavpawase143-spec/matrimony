package com.example.repository;

import com.example.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // 🔍 Find admin by username (Login support)
    Optional<Admin> findByUsername(String username);

    // 🔍 Find admin by email (Login / verification)
    Optional<Admin> findByEmail(String email);

    // ✅ Validation checks
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // 🔥 Optional (useful for login with either email or username)
    Optional<Admin> findByEmailOrUsername(String email, String username);
}