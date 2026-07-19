package com.example.serviceimpl;

import com.example.dto.request.UserFilterDTO;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.LoginResponse;
import com.example.dto.response.PageResponse;
import com.example.dto.response.ProfileResponseDTO;
import com.example.dto.response.UserResponseDTO;
import com.example.mapper.UserMapper;
import com.example.model.*;
import com.example.repository.*;
import com.example.security.JwtUtil;
import com.example.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final EmailVerificationRepository verificationRepository;
    private final PhoneVerificationOTPRepository otpRepository;
    private final SMSService smsService;
    private final ProfileService profileService;
    private final RefreshTokenService refreshTokenService;
    
    // Profile-related repositories
    private final ReligionRepository religionRepository;
    private final CasteRepository casteRepository;
    private final CityRepository cityRepository;
    private final MotherTongueRepository motherTongueRepository;
    private final MaritalStatusRepository maritalStatusRepository;
    private final EducationLevelRepository educationLevelRepository;
    private final OccupationRepository occupationRepository;
    private final HeightRepository heightRepository;
    private final WeightRepository weightRepository;

    // ================= REGISTER =================
    @Override
    public User register(UserRegisterRequestDTO request) {
        System.out.println("🚀 Starting user registration for email: " + request.getEmail());
        System.out.println("📋 Basic registration request data:");
        System.out.println("   - First Name: " + request.getFirstName());
        System.out.println("   - Last Name: " + request.getLastName());
        System.out.println("   - Phone: " + request.getPhone());


        if (userRepository.existsByEmail(request.getEmail())) {
            System.out.println("❌ Email already exists: " + request.getEmail());
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmailVerified(false);

        // ✅ DEFAULT ROLE
        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        user.setRoles(new HashSet<>(Set.of(role)));

        User savedUser = userRepository.save(user);
        System.out.println("✅ User saved with ID: " + savedUser.getId());

        // ✅ CREATE EMPTY PROFILE FOR USER
        try {
            if (savedUser != null && savedUser.getId() != null) {
                if (!profileRepository.existsByUser(savedUser)) {
                    Profile profile = new Profile();
                    profile.setUser(savedUser);
                    profile.setCurrentStep(1);
                    profile.setProfileCompleted(false);
                    profile.setIsActive(true);
                    
                    // Set basic fields from registration

                    
                    Profile savedProfile = profileRepository.save(profile);
                    System.out.println("✅ Empty profile created for user: " + savedUser.getEmail());
                    System.out.println("   - Profile ID: " + savedProfile.getId());
                    System.out.println("   - User ID: " + savedUser.getId());

                    
                    // Verify the profile was saved correctly
                    profileRepository.findById(savedProfile.getId()).ifPresentOrElse(
                        p -> System.out.println("✅ Profile verification: User=" + p.getUser().getEmail() + 
                            ", Profile ID=" + p.getId()),
                        () -> System.err.println("⚠️ Profile verification failed")
                    );
                } else {
                    System.out.println("ℹ️ Profile already exists for user: " + savedUser.getEmail());
                }
            } else {
                System.err.println("⚠️ Cannot create profile: User is null or ID is missing");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed to create empty profile for user " + 
                (savedUser != null ? savedUser.getEmail() : "unknown") + ": " + e.getMessage());
            e.printStackTrace();
            // Don't fail registration if profile creation fails
        }

        // ✅ SEND EMAIL (correct)
        sendVerification(user.getEmail());

        return user;
    }

    // ================= LOGIN =================
    @Override
    @Transactional
    public User login(String email, String password) {

        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password. Please check your credentials and try again.");
        }

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Email not verified. Please check your inbox and verify your email before logging in.");
        }

        if (!Boolean.TRUE.equals(user.getPhoneVerified())) {
            throw new RuntimeException("Phone number not verified. Please verify your phone number before logging in.");
        }

        // ✅ CHECK AND CREATE PROFILE IF MISSING
        try {
            if (!profileRepository.existsByUser(user)) {
                Profile profile = new Profile();
                profile.setUser(user);
                profile.setCurrentStep(1);
                profile.setProfileCompleted(false);
                profile.setIsActive(true);
                profileRepository.save(profile);
                System.out.println("✅ Basic profile created during login for user: " + user.getEmail());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed to create profile during login: " + e.getMessage());
            // Don't fail login if profile creation fails
        }


        // Update online status
        user.setIsOnline(true);
        user.setLastHeartbeat(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return savedUser;
    }

    // ================= LOGIN TOKEN =================
    @Override
    public String loginAndGenerateToken(String email, String password) {

        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password. Please check your credentials and try again.");
        }

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Email not verified. Please check your inbox and verify your email before logging in.");
        }

        if (!Boolean.TRUE.equals(user.getPhoneVerified())) {
            throw new RuntimeException("Phone number not verified. Please verify your phone number before logging in.");
        }

        // ✅ CHECK AND CREATE PROFILE IF MISSING
        try {
            if (!profileRepository.existsByUser(user)) {
                Profile profile = new Profile();
                profile.setUser(user);
                profile.setCurrentStep(1);
                profile.setProfileCompleted(false);
                profile.setIsActive(true);
                profileRepository.save(profile);
                System.out.println("✅ Basic profile created during token generation for user: " + user.getEmail());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed to create profile during token generation: " + e.getMessage());
            // Don't fail token generation if profile creation fails
        }

        // ================= UPDATE USER STATUS =================
        user.setIsOnline(true);
        user.setLastSeen(null);
        user.setLastLogin(LocalDateTime.now());

        userRepository.save(user);

        System.out.println(
                "✅ USER LOGIN -> "
                        + user.getEmail()
                        + " ONLINE = "
                        + user.getIsOnline()
        );

        List<String> roles = Optional.ofNullable(user.getRoles())
                .orElse(Set.of())
                .stream()
                .map(Role::getName)
                .toList();

        return jwtUtil.generateToken(user.getEmail(), roles);
    }

    // ================= LOGIN WITH PROFILE =================
    @Override
    @Transactional
    public LoginResponse loginWithProfile(String email, String password) {
        // Authenticate user
        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password. Please check your credentials and try again.");
        }

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Email not verified. Please check your inbox and verify your email before logging in.");
        }

        if (!Boolean.TRUE.equals(user.getPhoneVerified())) {
            throw new RuntimeException("Phone number not verified. Please verify your phone number before logging in.");
        }

        // Check and create profile if missing
        try {
            if (!profileRepository.existsByUser(user)) {
                Profile profile = new Profile();
                profile.setUser(user);
                profile.setCurrentStep(1);
                profile.setProfileCompleted(false);
                profile.setIsActive(true);
                profileRepository.save(profile);
                System.out.println("✅ Basic profile created during login for user: " + user.getEmail());
            }
        } catch (Exception e) {
            System.err.println("⚠️ Failed to create profile during login: " + e.getMessage());
        }

        // Update user status
        user.setLastLogin(LocalDateTime.now());
        user.setIsOnline(true);
        user.setLastSeen(null);

        userRepository.save(user);

        System.out.println(
                "USER LOGIN -> "
                        + user.getEmail()
                        + " ONLINE="
                        + user.getIsOnline()
        );

        // Generate tokens
        List<String> roles = Optional.ofNullable(user.getRoles())
                .orElse(Set.of())
                .stream()
                .map(Role::getName)
                .toList();
        String accessToken = jwtUtil.generateToken(user.getEmail(), roles);
        RefreshToken refreshToken = refreshTokenService.createToken(user.getEmail());

        // Get profile data
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

        return new LoginResponse(accessToken, refreshToken.getToken(), profileData);
    }

    // ================= EMAIL =================
    @Override
    public void sendVerification(String email) {

        String token = UUID.randomUUID().toString();

        EmailVerification ev = new EmailVerification();
        ev.setEmail(email);
        ev.setToken(token);
        ev.setVerified(false);
        ev.setExpiryDate(LocalDateTime.now().plusHours(24));

        verificationRepository.save(ev);

        // ✅ Use enhanced verification email
        emailService.sendVerificationEmail(email, token);
    }

    // ================= FORGOT PASSWORD =================
    @Override
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        EmailVerification ev = new EmailVerification();
        ev.setEmail(email);
        ev.setToken(token);
        ev.setVerified(false);
        ev.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        verificationRepository.save(ev);

        String link =
                "http://localhost:3000/reset-password?token=" + token;
        emailService.sendEmail(email, "Reset Password", "Click: " + link);
    }

    @Override
    public void resetPassword(String token, String newPassword) {

        EmailVerification ev = verificationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (ev.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = userRepository.findByEmail(ev.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        ev.setVerified(true);
        verificationRepository.save(ev);
    }

    // ================= VERIFY EMAIL =================
    @Override
    public void verifyEmail(String token) {

        EmailVerification ev = verificationRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (ev.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = userRepository.findByEmail(ev.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());

        userRepository.save(user);

        ev.setVerified(true);
        verificationRepository.save(ev);

        // 🎉 Send welcome email after successful verification
        try {
            emailService.sendWelcomeEmail(user.getEmail(), user.getFirstName());
        } catch (Exception e) {
            // Don't fail verification if welcome email fails
            System.err.println("⚠️ Welcome email failed to send: " + e.getMessage());
        }
    }

    @Override
    public void resendVerification(String email) {
        sendVerification(email);
    }

    @Override
    public void bypassEmailVerification(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setEmailVerified(true);
        userRepository.save(user);
        
        System.out.println("🔥 Development: Email verification bypassed for " + email);
    }

    @Override
    public void bypassPhoneVerification(String phone) {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found with phone: " + phone));
        
        user.setPhoneVerified(true);
        userRepository.save(user);
        
        System.out.println("🔥 Development: Phone verification bypassed for " + phone);
    }

    @Override
    @Transactional
    public void logout(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // ================= USER OFFLINE =================
        user.setIsOnline(false);
        user.setLastSeen(LocalDateTime.now());
        user.setLastHeartbeat(null);

        userRepository.saveAndFlush(user);

        System.out.println(
                "🔴 USER LOGOUT -> "
                        + user.getEmail()
                        + " | ONLINE = "
                        + user.getIsOnline()
                        + " | LAST SEEN = "
                        + user.getLastSeen()
        );
    }

    // ================= BASIC =================
    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> getById(Long id) {
        return userRepository.findByIdWithRoles(id)
                .map(UserMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAll() {
        return userRepository.findAllWithRoles()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getActiveUsers() {
        return userRepository.findActiveUsersWithRoles()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    // ================= UPDATE =================
    @Override
    public User update(Long id, User updatedUser) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updatedUser.getFirstName() != null)
            existingUser.setFirstName(updatedUser.getFirstName());

        if (updatedUser.getLastName() != null)
            existingUser.setLastName(updatedUser.getLastName());

        if (updatedUser.getPhone() != null)
            existingUser.setPhone(updatedUser.getPhone());



        return userRepository.save(existingUser);
    }

    // ================= DELETE =================
    @Override
    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getRoles().clear();

        if (user.getProfile() != null) user.setProfile(null);
        if (user.getPartnerPreference() != null) user.setPartnerPreference(null);

        userRepository.save(user);
        userRepository.delete(user);
    }

    // ================= DEACTIVATE =================
    @Override
    public void deactivate(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsActive(false);


        userRepository.save(user);
    }

    // ================= SEARCH =================
    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> search(String keyword) {

        if (keyword == null || keyword.isBlank()) return List.of();

        return userRepository.searchWithRoles(keyword)
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    // ================= PAGINATION =================
    @Override
    public PageResponse<UserResponseDTO> getAllUsers(
            int page, int size, String sortBy, String direction, UserFilterDTO filter) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDTO> content = userPage.getContent()
                .stream()
                .map(UserMapper::toDTO)
                .toList();

        return new PageResponse<>(
                content,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
    }

    // ================= INTERNAL =================
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveVerificationToken(Long userId, String token) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EmailVerification ev = new EmailVerification();
        ev.setEmail(user.getEmail());
        ev.setToken(token);
        ev.setVerified(false);
        ev.setExpiryDate(LocalDateTime.now().plusHours(24));

        verificationRepository.save(ev);
    }

    // ================= PHONE VERIFICATION =================

    @Override
    public String sendOTPToPhone(String phone) {

        // ✅ Generate OTP
        String otp = generateOTP();

        // ✅ Delete old OTP
        otpRepository.findByPhone(phone).ifPresent(otpRepository::delete);

        // ✅ Save new OTP
        PhoneVerificationOTP phoneOTP = new PhoneVerificationOTP(phone, otp);
        phoneOTP.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        phoneOTP.setAttemptCount(0);

        otpRepository.save(phoneOTP);

        // ❌ SMS disabled (free testing)
        // smsService.sendOTP(phone, otp);

        System.out.println("✅ OTP generated: " + otp);

        return otp;
    }

    @Override
    public void verifyPhoneOTP(String phone, String otp) {

        PhoneVerificationOTP phoneOTP = otpRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("OTP not found. Please request a new one."));

        if (phoneOTP.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired. Please request a new one.");
        }

        if (phoneOTP.getAttemptCount() >= 3) {
            throw new RuntimeException("Too many failed attempts. Please request a new OTP.");
        }

        if (!phoneOTP.getOtp().equals(otp)) {
            phoneOTP.setAttemptCount(phoneOTP.getAttemptCount() + 1);
            otpRepository.save(phoneOTP);
            throw new RuntimeException("Invalid OTP. Attempts remaining: " + (3 - phoneOTP.getAttemptCount()));
        }

        // ✅ OTP verified successfully
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPhoneVerified(true);
        user.setPhoneVerifiedAt(LocalDateTime.now());
        userRepository.save(user);

        // ✅ Clean up used OTP
        otpRepository.delete(phoneOTP);

        System.out.println("✅ PHONE VERIFIED: " + phone);
    }

    @Override
    public void resendPhoneOTP(String phone) {
        sendOTPToPhone(phone);
    }

    // ================= UTILITY METHODS =================

    /**
     * Generate 6-digit OTP
     */
    private String generateOTP() {
        return String.format("%06d", new java.util.Random().nextInt(1000000));
    }
}

