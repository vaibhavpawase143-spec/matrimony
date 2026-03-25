package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // =========================
    // 🔐 LOGIN (ACTIVE USERS)
    // =========================
    Optional<User> findByEmailIgnoreCaseAndIsActiveTrue(String email);

    // =========================
    // 🔍 FIND BY EMAIL
    // =========================
    Optional<User> findByEmailIgnoreCase(String email);

    // =========================
    // 🔍 CHECK EMAIL EXISTS
    // =========================
    boolean existsByEmailIgnoreCase(String email);

    // =========================
    // 📱 PHONE
    // =========================
    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    // =========================
    // 🔍 DUPLICATE CHECK
    // =========================
    boolean existsByEmailIgnoreCaseOrPhone(String email, String phone);

    // =========================
    // 🔍 GET ACTIVE USERS
    // =========================
    List<User> findByIsActiveTrue();

    // =========================
    // 🔍 GET ACTIVE USER BY ID
    // =========================
    Optional<User> findByIdAndIsActiveTrue(Long id);

    // =========================
    // 🔍 SEARCH (FULL)
    // =========================
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName,
            String lastName,
            String email
    );
}