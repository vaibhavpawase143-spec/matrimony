package com.example.repository;

import com.example.model.RefreshToken;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByEmail(String email);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.email = :email")
    void deleteByEmail(@Param("email") String email);
}