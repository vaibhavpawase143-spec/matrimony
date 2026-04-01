package com.example.serviceimpl;

import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.PageResponse;
import com.example.dto.response.UserResponseDTO;
import com.example.model.*;

import com.example.repository.*;
import com.example.service.*;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // ================= REGISTER =================
    @Override
    public User register(UserRegisterRequestDTO dto) {

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);

        String token = UUID.randomUUID().toString();

        VerificationToken vt = new VerificationToken();
        vt.setToken(token);
        vt.setUser(savedUser);
        vt.setExpiryDate(LocalDateTime.now().plusMinutes(15));

        verificationTokenRepository.save(vt);

        // ✅ FIXED LINK
        String link = "http://localhost:9090/api/auth/verify?token=" + token;

        emailService.sendEmail(
                savedUser.getEmail(),
                "Verify Your Email",
                "Click here to verify your email: " + link
        );

        return savedUser;
    }

    // ================= LOGIN =================
    @Override
    public User login(String email, String password) {

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getEmailVerified()) {
            throw new RuntimeException("Please verify your email first");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    @Override
    public String loginAndGenerateToken(String email, String password) {
        login(email, password);
        return "JWT_TOKEN"; // Replace with actual JWT logic
    }

    // ================= EMAIL =================
    @Override
    public void verifyEmail(String token) {

        VerificationToken vt = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (vt.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = vt.getUser();
        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public void resendVerification(String email) {
        // You can improve this later
        throw new RuntimeException("Resend verification not implemented yet");
    }

    // ================= PASSWORD =================
    @Override
    public void forgotPassword(String email) {

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<PasswordResetToken> existing =
                passwordResetTokenRepository.findByUser(user);

        String token = UUID.randomUUID().toString();

        if (existing.isPresent()) {
            PasswordResetToken t = existing.get();
            t.setToken(token);
            t.setExpiryDate(LocalDateTime.now().plusMinutes(15));
            passwordResetTokenRepository.save(t);
        } else {
            PasswordResetToken t = new PasswordResetToken();
            t.setToken(token);
            t.setUser(user);
            t.setExpiryDate(LocalDateTime.now().plusMinutes(15));
            passwordResetTokenRepository.save(t);
        }

        // ✅ IMPORTANT FIX (TOKEN ADDED)
        String link = "http://localhost:9090/api/auth/reset-password?token=" + token;

        emailService.sendEmail(
                user.getEmail(),
                "Reset Your Password",
                "Click here to reset your password: " + link
        );
    }

    @Override
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken t = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (t.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = t.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);
        passwordResetTokenRepository.delete(t);
    }

    // ================= USER =================
    @Override
    public Optional<UserResponseDTO> getById(Long id) {
        return userRepository.findById(id).map(this::mapToDTO);
    }

    @Override
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream().map(this::mapToDTO).toList();
    }

    @Override
    public List<UserResponseDTO> getActiveUsers() {
        return userRepository.findByIsActiveTrue().stream().map(this::mapToDTO).toList();
    }

    @Override
    public List<User> search(String keyword) {
        return userRepository.findByEmailContainingIgnoreCase(keyword);
    }

    @Override
    public User update(Long id, User updated) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(updated.getFirstName());
        user.setLastName(updated.getLastName());
        user.setPhone(updated.getPhone());

        return userRepository.save(user);
    }

    @Override
    public void deactivate(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsActive(false);
        userRepository.save(user);
    }

    // ================= PAGINATION =================
    @Override
    public PageResponse<UserResponseDTO> getAllUsers(int page, int size) {

        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size));
        return buildPageResponse(userPage);
    }

    @Override
    public PageResponse<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size, sort));
        return buildPageResponse(userPage);
    }

    // ================= HELPER =================
    private PageResponse<UserResponseDTO> buildPageResponse(Page<User> page) {

        List<UserResponseDTO> content = page.getContent()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    private UserResponseDTO mapToDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .build();
    }
}