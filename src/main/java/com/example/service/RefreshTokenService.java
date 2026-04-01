package com.example.service;

import com.example.model.RefreshToken;
import com.example.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional; // ✅ IMPORTANT
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    private final long REFRESH_DURATION = 7 * 24 * 60 * 60 * 1000; // 7 days

    // 🔐 CREATE TOKEN
    public RefreshToken createToken(String email) {

        // delete old token
        repository.deleteByEmail(email);

        RefreshToken token = RefreshToken.builder()
                .email(email)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(REFRESH_DURATION))
                .build();

        return repository.save(token);
    }

    // 🔍 VERIFY TOKEN
    public RefreshToken verifyToken(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }

    // 🚪 LOGOUT (DELETE TOKEN)
    @Transactional  // 🔥 VERY IMPORTANT FIX
    public void deleteByEmail(String email) {
        repository.deleteByEmail(email);
    }
}