package com.example.service;

import com.example.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserVerificationService {

    Page<User> getUsersWithPendingEmailVerification(Pageable pageable);
    
    Page<User> getUsersWithPendingPhoneVerification(Pageable pageable);
    
    void verifyEmailByAdmin(Long userId);
    
    void verifyPhoneByAdmin(Long userId);
    
    void verifyBothByAdmin(Long userId);
    
    void rejectVerification(Long userId, String reason);
    
    Long getPendingEmailVerificationCount();
    
    Long getPendingPhoneVerificationCount();
    
    Long getVerifiedEmailCount();
    
    Long getVerifiedPhoneCount();
    
    Long getFullyVerifiedCount();
}
