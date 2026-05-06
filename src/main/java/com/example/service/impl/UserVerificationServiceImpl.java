package com.example.serviceimpl;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserVerificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserVerificationServiceImpl implements UserVerificationService {

    private final UserRepository userRepository;

    @Override
    public Page<User> getUsersWithPendingEmailVerification(Pageable pageable) {
        return userRepository.findByEmailVerifiedFalseAndIsDeletedFalse(pageable);
    }

    @Override
    public Page<User> getUsersWithPendingPhoneVerification(Pageable pageable) {
        return userRepository.findByPhoneVerifiedFalseAndIsDeletedFalse(pageable);
    }

    @Override
    @Transactional
    public void verifyEmailByAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Email verified by admin for user: {}", userId);
    }

    @Override
    @Transactional
    public void verifyPhoneByAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPhoneVerified(true);
        user.setPhoneVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Phone verified by admin for user: {}", userId);
    }

    @Override
    @Transactional
    public void verifyBothByAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailVerified(true);
        user.setPhoneVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());
        user.setPhoneVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("Email and phone verified by admin for user: {}", userId);
    }

    @Override
    @Transactional
    public void rejectVerification(Long userId, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailVerified(false);
        user.setPhoneVerified(false);
        user.setEmailVerifiedAt(null);
        user.setPhoneVerifiedAt(null);
        userRepository.save(user);

        log.info("Verification rejected for user: {}, reason: {}", userId, reason);
    }

    @Override
    public Long getPendingEmailVerificationCount() {
        return userRepository.countByEmailVerifiedFalseAndIsDeletedFalse();
    }

    @Override
    public Long getPendingPhoneVerificationCount() {
        return userRepository.countByPhoneVerifiedFalseAndIsDeletedFalse();
    }

    @Override
    public Long getVerifiedEmailCount() {
        return userRepository.countByEmailVerifiedTrueAndIsDeletedFalse();
    }

    @Override
    public Long getVerifiedPhoneCount() {
        return userRepository.countByPhoneVerifiedTrueAndIsDeletedFalse();
    }

    @Override
    public Long getFullyVerifiedCount() {
        return userRepository.countByEmailVerifiedTrueAndPhoneVerifiedTrueAndIsDeletedFalse();
    }
}
