package com.example.serviceimpl;

import com.example.model.User;
import com.example.model.PasswordResetToken;
import com.example.repository.UserRepository;
import com.example.repository.PasswordResetTokenRepository;
import com.example.service.PasswordResetService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    // ================= FORGOT PASSWORD =================
    @Override
    @Transactional
    public String createResetToken(String email) {

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 Remove existing token if present
        tokenRepository.deleteByUser(user);

        // 🔐 Generate new token
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        tokenRepository.save(resetToken);

        return token;
    }

    // ================= RESET PASSWORD =================
    @Override
    @Transactional
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        // ⏰ Check expiry
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();

        // 🔐 Encode new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // 🧹 Delete token after use
        tokenRepository.delete(resetToken);
    }
}