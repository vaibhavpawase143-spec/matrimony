package com.example.serviceimpl;

import com.example.model.RefreshToken;
import com.example.repository.RefreshTokenRepository;
import com.example.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repository;

    private final long REFRESH_DURATION = 7 * 24 * 60 * 60 * 1000; // 7 days

    // ================= CREATE TOKEN =================
    @Override
    @Transactional   // 🔥 FIX (IMPORTANT)
    public RefreshToken createToken(String email) {

        // 🔥 delete old token
        repository.deleteByEmail(email);

        RefreshToken token = RefreshToken.builder()
                .email(email)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(REFRESH_DURATION))
                .build();

        return repository.save(token);
    }

    // ================= VERIFY TOKEN =================
    @Override
    public RefreshToken verifyToken(String token) {

        RefreshToken refreshToken = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }

    // ================= DELETE =================
    @Override
    @Transactional   // 🔥 FIX (IMPORTANT)
    public void deleteByEmail(String email) {
        repository.deleteByEmail(email);
    }
}