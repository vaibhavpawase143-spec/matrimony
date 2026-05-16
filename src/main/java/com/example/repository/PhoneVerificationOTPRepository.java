package com.example.repository;

import com.example.model.PhoneVerificationOTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneVerificationOTPRepository extends JpaRepository<PhoneVerificationOTP, Long> {

    Optional<PhoneVerificationOTP> findByPhone(String phone);

    Optional<PhoneVerificationOTP> findByPhoneAndOtp(String phone, String otp);

    Optional<PhoneVerificationOTP> findTopByPhoneOrderByCreatedAtDesc(String phone);
}

