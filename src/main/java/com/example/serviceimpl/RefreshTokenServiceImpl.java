package com.example.serviceimpl;

import com.example.model.RefreshToken;
import com.example.repository.AdminRepository;
import com.example.repository.RefreshTokenRepository;
import com.example.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    private static final long REFRESH_DURATION = 7 * 24 * 60 * 60 * 1000L; // 7 days

    // =====================================================
    // CREATE / UPDATE REFRESH TOKEN
    // =====================================================
    @Override
    @Transactional
    public RefreshToken createToken(String email) {

        repository.deleteByEmail(email);

        RefreshToken refreshToken = RefreshToken.builder()
                .email(email)
                .token(UUID.randomUUID().toString())
                .expiryDate(
                        Instant.now().plusMillis(REFRESH_DURATION)
                )
                .build();

        return repository.saveAndFlush(refreshToken);
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

        userRepository.findByEmailIgnoreCase(refreshToken.getEmail()).ifPresent(user -> {
            if (!Boolean.TRUE.equals(user.getIsActive()) || Boolean.TRUE.equals(user.getIsDeleted())) {
                repository.delete(refreshToken);
                throw new RuntimeException("Account is deactivated or deleted");
            }
        });

        adminRepository.findByEmailIgnoreCase(refreshToken.getEmail()).ifPresent(admin -> {
            if (!Boolean.TRUE.equals(admin.getIsActive())) {
                repository.delete(refreshToken);
                throw new RuntimeException("Admin account is deactivated");
            }
        });

        return refreshToken;
    }

    // =====================================================
    // ATOMIC ROTATE REFRESH TOKEN (SINGLE USE PESSIMISTIC LOCK)
    // =====================================================
    @Override
    @Transactional
    public RefreshToken rotateToken(String oldTokenString) {
        if (oldTokenString == null || oldTokenString.trim().isEmpty()) {
            throw new RuntimeException("Invalid refresh token");
        }

        // Lock row pessimistically to prevent concurrent reuse race conditions
        RefreshToken oldToken = repository.findByTokenForUpdate(oldTokenString.trim())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (oldToken.getExpiryDate().isBefore(Instant.now())) {
            repository.delete(oldToken);
            repository.flush();
            throw new RuntimeException("Refresh token expired");
        }

        String email = oldToken.getEmail();

        // Validate user account status if user exists
        userRepository.findByEmailIgnoreCase(email).ifPresent(user -> {
            if (!Boolean.TRUE.equals(user.getIsActive())
                    || Boolean.TRUE.equals(user.getIsDeleted())
                    || Boolean.TRUE.equals(user.getIsBlocked())) {
                repository.delete(oldToken);
                repository.flush();
                throw new RuntimeException("Account is deactivated or deleted");
            }
        });

        // Validate admin account status if admin exists
        adminRepository.findByEmailIgnoreCase(email).ifPresent(admin -> {
            if (!Boolean.TRUE.equals(admin.getIsActive())) {
                repository.delete(oldToken);
                repository.flush();
                throw new RuntimeException("Admin account is deactivated");
            }
        });

        // Atomically delete consumed old token
        repository.delete(oldToken);
        repository.flush();

        // Generate and persist replacement token
        RefreshToken newToken = RefreshToken.builder()
                .email(email)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(REFRESH_DURATION))
                .build();

        return repository.saveAndFlush(newToken);
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