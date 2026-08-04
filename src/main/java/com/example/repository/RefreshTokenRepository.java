package com.example.repository;

import com.example.model.RefreshToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByEmail(String email);

    @Transactional
    @Modifying
    @Query(
            value = "DELETE FROM refresh_token WHERE email = :email",
            nativeQuery = true
    )
    void deleteByEmail(@Param("email") String email);
}