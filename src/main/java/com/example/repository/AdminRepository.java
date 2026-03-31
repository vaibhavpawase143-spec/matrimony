package com.example.repository;

import com.example.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // 🔍 Find admin by username (Login support)
    Optional<Admin> findByUsername(String username);

    // 🔥 FIXED: Case-insensitive email search (IMPORTANT)
    Optional<Admin> findByEmailIgnoreCase(String email);

    // ✅ Validation checks
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // 🔥 Optional (login using email OR username)
    Optional<Admin> findByEmailIgnoreCaseOrUsername(String email, String username);
}