package com.example.serviceimpl;

import com.example.dto.request.UserFilterDTO;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.PageResponse;
import com.example.dto.response.UserResponseDTO;
import com.example.mapper.UserMapper;
import com.example.model.Admin;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.security.JwtUtil;
import com.example.service.UserBlockService;
import com.example.service.UserService;
import com.example.specification.UserSpecification;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserBlockService userBlockService;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

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

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);

        return userRepository.save(user);
    }

    // ================= LOGIN =================
    @Override
    public User login(String email, String password) {

        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.getIsDeleted())) {
            throw new RuntimeException("Account is deleted");
        }

        if (!Boolean.TRUE.equals(user.getIsActive())) {
            throw new RuntimeException("Account is inactive");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        return user;
    }

    // ================= LOGIN + TOKEN =================
    @Override
    public String loginAndGenerateToken(String email, String password) {

        Optional<Admin> adminOpt = adminRepository.findByEmailWithRole(email);

        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();

            if (!Boolean.TRUE.equals(admin.getIsActive())) {
                throw new RuntimeException("Admin is inactive");
            }

            if (!passwordEncoder.matches(password, admin.getPassword())) {
                throw new RuntimeException("Invalid password");
            }

            admin.setLastLogin(LocalDateTime.now());
            adminRepository.save(admin);

            return jwtUtil.generateToken(
                    admin.getEmail(),
                    List.of(admin.getRole().getName())
            );
        }

        User user = login(email, password);

        return jwtUtil.generateToken(
                user.getEmail(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList())
        );
    }

    // ================= GET ALL =================
    @Override
    public List<UserResponseDTO> getAll() {
        return userRepository.findAllWithRoles()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    // ================= GET BY ID =================
    @Override
    public Optional<UserResponseDTO> getById(Long id) {
        return userRepository.findByIdWithRoles(id)
                .map(UserMapper::toDTO);
    }

    // ================= ACTIVE USERS =================
    @Override
    public List<UserResponseDTO> getActiveUsers() {
        return userRepository.findActiveUsersWithRoles()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    // ================= SEARCH =================
    @Override
    @Transactional
    public List<UserResponseDTO> search(String keyword) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<User> users = userRepository.findByEmailContainingIgnoreCase(keyword);

        // 🔥 FILTER + MAP INSIDE TRANSACTION
        return users.stream()
                .filter(u -> !userBlockService.isBlocked(currentUser.getId(), u.getId()))
                .map(UserMapper::toDTO)   // ✅ MOVE HERE
                .toList();
    }// ================= UPDATE =================

    @Override
    public User update(Long id, User updatedUser) {

        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long adminId = getCurrentAdminId();

        if (updatedUser.getFirstName() != null)
            existing.setFirstName(updatedUser.getFirstName());

        if (updatedUser.getLastName() != null)
            existing.setLastName(updatedUser.getLastName());

        if (updatedUser.getPhone() != null)
            existing.setPhone(updatedUser.getPhone());

        existing.setUpdatedBy(adminId);

        return userRepository.save(existing);
    }
    // ================= DELETE =================
    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Admin admin = adminRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Only admin can delete users"));

        user.setIsDeleted(true);
        user.setIsActive(false);
        user.setDeletedBy(admin.getId());
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    private Long getCurrentAdminId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Admin admin = adminRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return admin.getId();
    }

    // ================= PAGINATION =================
    @Override
    public PageResponse<UserResponseDTO> getAllUsers(
            int page,
            int size,
            String sortBy,
            String direction,
            UserFilterDTO filter
    ) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<User> spec = UserSpecification.getUsers(filter);

        Page<User> userPage = userRepository.findAll(spec, pageable);

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

    // ================= OPTIONAL =================
    @Override public void deactivate(Long id) {}
    @Override public void verifyEmail(String token) {}
    @Override public void resendVerification(String email) {}
    @Override public void forgotPassword(String email) {}
    @Override public void resetPassword(String token, String newPassword) {}
    @Override public void sendVerification(String email) {}

    // ================= 🔥 FIXED METHOD =================
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}