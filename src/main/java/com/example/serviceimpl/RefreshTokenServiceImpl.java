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

    private static final long REFRESH_DURATION = 7 * 24 * 60 * 60 * 1000L; // 7 days

    // =====================================================
    // CREATE / UPDATE REFRESH TOKEN
    // =====================================================
    @Override
    @Transactional
    public RefreshToken createToken(String email) {

        repository.deleteByEmail(email);
        repository.flush();

        RefreshToken refreshToken = RefreshToken.builder()
                .email(email)
                .token(UUID.randomUUID().toString())
                .expiryDate(
                        Instant.now().plusMillis(REFRESH_DURATION)
                )
                .build();

        return repository.save(refreshToken);
    }
    // =====================================================
    // VERIFY REFRESH TOKEN
    // =====================================================
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

    // =====================================================
    // DELETE REFRESH TOKEN
    // =====================================================
    @Override
    @Transactional
    public void deleteByEmail(String email) {

        repository.findByEmail(email)
                .ifPresent(repository::delete);
    }
}