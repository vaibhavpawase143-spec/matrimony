package com.example.serviceimpl;

import com.example.dto.request.SendOtpRequestDTO;
import com.example.dto.request.VerifyOtpRequestDTO;
import com.example.dto.response.LoginResponse;
import com.example.dto.response.OtpStatusResponseDTO;
import com.example.dto.response.ProfileResponseDTO;
import com.example.model.PhoneVerificationOTP;
import com.example.model.RefreshToken;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.PhoneVerificationOTPRepository;
import com.example.repository.ProfileRepository;
import com.example.repository.UserRepository;
import com.example.security.JwtUtil;
import com.example.service.EmailService;
import com.example.service.RealtimeOTPService;
import com.example.service.RefreshTokenService;
import com.example.service.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RealtimeOTPServiceImpl implements RealtimeOTPService {

    private static final int COOLDOWN_SECONDS = 60;
    private static final int EXPIRY_MINUTES = 10;
    private static final int MAX_ATTEMPTS = 3;

    private final PhoneVerificationOTPRepository otpRepository;
    private final SMSService smsService;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;
    private final ProfileRepository profileRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Value("${app.sms.enabled:false}")
    private boolean smsEnabled;

    @Override
    @Transactional
    public OtpStatusResponseDTO sendOTP(SendOtpRequestDTO request) {
        if (request.getTarget() == null || request.getTarget().trim().isEmpty()) {
            throw new RuntimeException("Target phone number or email is required");
        }

        String target = request.getTarget().trim();
        String channel = determineChannel(target, request.getChannel());
        String purpose = request.getPurpose() != null ? request.getPurpose().toUpperCase() : "VERIFICATION";

        // Check rate limiting / cooldown
        Optional<PhoneVerificationOTP> existingOtp = otpRepository.findByPhoneAndPurpose(target, purpose);
        if (existingOtp.isPresent()) {
            PhoneVerificationOTP otpObj = existingOtp.get();
            if (otpObj.isCooldownActive(COOLDOWN_SECONDS)) {
                long remainingSeconds = otpObj.getRemainingCooldownSeconds(COOLDOWN_SECONDS);
                log.warn("Rate limit triggered for target: {}. Wait {} seconds", target, remainingSeconds);
                throw new RuntimeException("Please wait " + remainingSeconds + " seconds before requesting a new OTP.");
            }
        }

        // Generate 6-digit cryptographically secure OTP
        String otpCode = generateSecureOTP();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(EXPIRY_MINUTES);
        LocalDateTime now = LocalDateTime.now();

        PhoneVerificationOTP otpEntity = existingOtp.orElseGet(PhoneVerificationOTP::new);
        otpEntity.setPhone(target);
        otpEntity.setOtp(otpCode);
        otpEntity.setTargetType(channel);
        otpEntity.setPurpose(purpose);
        otpEntity.setVerified(false);
        otpEntity.setAttemptCount(0);
        otpEntity.setLastSentAt(now);
        otpEntity.setExpiryDate(expiry);

        otpRepository.save(otpEntity);
        log.info("Generated OTP for target: {}, channel: {}, purpose: {}", target, channel, purpose);

        // Deliver OTP via selected channel
        if ("EMAIL".equalsIgnoreCase(channel)) {
            emailService.sendOTPEmail(target, otpCode, purpose);
        } else {
            smsService.sendOTP(target, otpCode);
        }

        return OtpStatusResponseDTO.builder()
                .success(true)
                .message("OTP sent successfully via " + channel)
                .target(target)
                .channel(channel)
                .purpose(purpose)
                .cooldownSeconds(COOLDOWN_SECONDS)
                .expiresInSeconds(EXPIRY_MINUTES * 60L)
                .remainingAttempts(MAX_ATTEMPTS)
                .devOtp(!smsEnabled ? otpCode : null)
                .build();
    }

    @Override
    @Transactional
    public OtpStatusResponseDTO resendOTP(String target, String purpose) {
        SendOtpRequestDTO request = SendOtpRequestDTO.builder()
                .target(target)
                .purpose(purpose)
                .build();
        return sendOTP(request);
    }

    @Override
    @Transactional
    public boolean verifyOTP(VerifyOtpRequestDTO request) {
        if (request.getTarget() == null || request.getTarget().trim().isEmpty()) {
            throw new RuntimeException("Target phone or email is required");
        }
        if (request.getOtp() == null || request.getOtp().trim().isEmpty()) {
            throw new RuntimeException("OTP code is required");
        }

        String target = request.getTarget().trim();
        String otp = request.getOtp().trim();
        String purpose = request.getPurpose() != null ? request.getPurpose().toUpperCase() : "VERIFICATION";

        PhoneVerificationOTP otpEntity = otpRepository.findByPhoneAndPurpose(target, purpose)
                .or(() -> otpRepository.findByPhone(target))
                .orElseThrow(() -> new RuntimeException("No OTP request found for this phone/email. Please request a new OTP."));

        // Expiry check
        if (otpEntity.isExpired()) {
            otpRepository.delete(otpEntity);
            throw new RuntimeException("OTP has expired. Please request a new OTP.");
        }

        // Max attempt check
        if (otpEntity.isMaxAttemptsReached()) {
            otpRepository.delete(otpEntity);
            throw new RuntimeException("Maximum OTP validation attempts reached. Please request a new OTP.");
        }

        // OTP code match check
        if (!otpEntity.getOtp().equals(otp)) {
            otpEntity.incrementAttempt();
            otpRepository.save(otpEntity);
            int remaining = MAX_ATTEMPTS - otpEntity.getAttemptCount();
            if (remaining <= 0) {
                otpRepository.delete(otpEntity);
                throw new RuntimeException("Invalid OTP code. Maximum attempts reached. Please request a new OTP.");
            }
            throw new RuntimeException("Invalid OTP code. " + remaining + " attempt(s) remaining.");
        }

        // Verification success! Mark verified and cleanup OTP
        otpEntity.setVerified(true);
        otpRepository.delete(otpEntity);

        // Update user account verification status if present
        Optional<User> userOpt = target.contains("@")
                ? userRepository.findByEmail(target)
                : userRepository.findByPhone(target);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if ("EMAIL".equalsIgnoreCase(otpEntity.getTargetType()) || target.contains("@")) {
                user.setEmailVerified(true);
                user.setEmailVerifiedAt(LocalDateTime.now());
            } else {
                user.setPhoneVerified(true);
                user.setPhoneVerifiedAt(LocalDateTime.now());
            }
            userRepository.save(user);
            log.info("User {} verification updated successfully", target);
        }

        return true;
    }

    @Override
    @Transactional
    public LoginResponse loginWithOTP(VerifyOtpRequestDTO request) {
        String target = request.getTarget().trim();
        request.setPurpose("LOGIN");

        // Verify OTP code first
        verifyOTP(request);

        // Find user by phone or email
        User user = (target.contains("@") ? userRepository.findByEmail(target) : userRepository.findByPhone(target))
                .orElseThrow(() -> new RuntimeException("No account registered with this " + (target.contains("@") ? "email" : "phone number")));

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("Your account has been deleted. Please contact support.");
        }

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new RuntimeException("Your account has been deactivated. Please contact support.");
        }

        // Update user online status and last login
        user.setLastLogin(LocalDateTime.now());
        user.setIsOnline(true);
        user.setLastSeen(null);

        if (target.contains("@")) {
            user.setEmailVerified(true);
        } else {
            user.setPhoneVerified(true);
        }
        userRepository.save(user);

        // Generate tokens
        List<String> roles = Optional.ofNullable(user.getRoles())
                .orElse(Set.of())
                .stream()
                .map(Role::getName)
                .toList();

        String accessToken = jwtUtil.generateToken(user.getEmail(), roles, null, "USER");
        RefreshToken refreshToken = refreshTokenService.createToken(user.getEmail());

        // Fetch user profile info
        ProfileResponseDTO profileData = profileRepository.findByUserId(user.getId())
                .map(profile -> {
                    ProfileResponseDTO dto = new ProfileResponseDTO();
                    dto.setId(profile.getId());
                    dto.setUserId(profile.getUser().getId());
                    dto.setUserName(profile.getUser().getFullName());
                    dto.setCurrentStep(profile.getCurrentStep());
                    dto.setProfileCompleted(profile.getProfileCompleted());
                    dto.setIsActive(profile.getIsActive());
                    dto.setCreatedAt(profile.getCreatedAt());
                    dto.setUpdatedAt(profile.getUpdatedAt());
                    return dto;
                })
                .orElse(null);

        String primaryRole = roles.isEmpty() ? null : roles.get(0);

        log.info("OTP Login successful for user: {}", user.getEmail());

        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                primaryRole,
                profileData
        );
    }

    private String determineChannel(String target, String inputChannel) {
        if (inputChannel != null && !inputChannel.trim().isEmpty()) {
            return inputChannel.trim().toUpperCase();
        }
        return target.contains("@") ? "EMAIL" : "PHONE";
    }

    private String generateSecureOTP() {
        int number = secureRandom.nextInt(900000) + 100000;
        return String.valueOf(number);
    }
}
