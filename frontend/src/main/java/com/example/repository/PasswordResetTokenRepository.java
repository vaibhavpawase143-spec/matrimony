package com.example.repository;

import com.example.model.PasswordResetToken;
import com.example.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    // 🔍 Find token by token string
    Optional<PasswordResetToken> findByToken(String token);

    // 🔍 Find token by user
    Optional<PasswordResetToken> findByUser(User user);

    // 🧹 Delete existing token for a user
    @Modifying
    void deleteByUser(User user);
}