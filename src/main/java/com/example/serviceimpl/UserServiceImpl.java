package com.example.serviceimpl;

import com.example.dto.request.UserFilterDTO;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.PageResponse;
import com.example.dto.response.UserResponseDTO;
import com.example.mapper.UserMapper;
import com.example.model.*;
import com.example.repository.*;
import com.example.security.JwtUtil;
import com.example.service.*;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
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
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final EmailVerificationRepository verificationRepository;

    // ================= REGISTER =================
    @Override
    public User register(UserRegisterRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
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

        userRepository.save(user);

        // ✅ SEND EMAIL (correct)
        sendVerification(user.getEmail());

        return user;
    }

    // ================= LOGIN =================
    @Override
    @Transactional
    public User login(String email, String password) {

        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Please verify your email first");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        user.setLastLogin(LocalDateTime.now());

        return userRepository.save(user);
    }

    // ================= LOGIN TOKEN =================
    @Override
    public String loginAndGenerateToken(String email, String password) {

        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException("Please verify your email first");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        List<String> roles = Optional.ofNullable(user.getRoles())
                .orElse(Set.of())
                .stream()
                .map(Role::getName)
                .toList();

        return jwtUtil.generateToken(user.getEmail(), roles);
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

        String link = "http://localhost:9090/api/users/verify?token=" + token;

        emailService.sendEmail(email, "Verify your account", "Click: " + link);
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

        String link = "http://localhost:9090/api/users/reset-password?token=" + token;

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
    }

    @Override
    public void resendVerification(String email) {
        sendVerification(email);
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

        existingUser.setUpdatedAt(LocalDateTime.now());

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
        user.setUpdatedAt(LocalDateTime.now());

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
}