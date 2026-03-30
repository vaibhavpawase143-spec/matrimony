package com.example.serviceimpl;

import com.example.dto.request.UserRegisterRequestDTO;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.JwtUtil;
import com.example.service.RoleService;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    // =========================
    // ✅ REGISTER (ENTITY)
    // =========================
    @Override
    public User register(User user) {

        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleService.getByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        user.setRoles(Set.of(role));
        user.setIsActive(true);

        return userRepository.save(user);
    }

    // =========================
    // ✅ REGISTER (DTO)
    // =========================
    @Override
    public User register(UserRegisterRequestDTO request) {

        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleService.getByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRoles(Set.of(role));
        user.setIsActive(true);

        return userRepository.save(user);
    }

    // =========================
    // 🔐 LOGIN
    // =========================
    @Override
    public User login(String email, String password) {

        User user = userRepository
                .findByEmailIgnoreCaseAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("User not found or inactive"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    // =========================
    // 🔥 JWT LOGIN
    // =========================
    @Override
    public String loginAndGenerateToken(String email, String password) {

        User user = userRepository
                .findByEmailIgnoreCaseAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("User not found or inactive"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        return jwtUtil.generateToken(user.getEmail(), roles);
    }

    // =========================
    // 🔍 GET BY ID
    // =========================
    @Override
    public Optional<User> getById(Long id) {
        return userRepository.findByIdAndIsActiveTrue(id);
    }

    // =========================
    // 🔍 GET BY EMAIL
    // =========================
    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    // =========================
    // 🔍 GET ALL
    // =========================
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // =========================
    // 🔍 ACTIVE USERS
    // =========================
    @Override
    public List<User> getActiveUsers() {
        return userRepository.findByIsActiveTrue();
    }

    // =========================
    // 🔍 SEARCH
    // =========================
    @Override
    public List<User> search(String keyword) {
        return userRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword
                );
    }

    // =========================
    // ✅ UPDATE (FINAL OWNERSHIP FIX)
    // =========================
    @Override
    public User update(Long id, User updatedUser) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Logged user not found"));

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = currentUser.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        if (!isAdmin && !user.getEmail().equals(email)) {
            throw new RuntimeException("You can only update your own data");
        }

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setPhone(updatedUser.getPhone());

        return userRepository.save(user);
    }

    // =========================
    // ❌ DEACTIVATE (FINAL OWNERSHIP FIX)
    // =========================
    @Override
    public void deactivate(Long id) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Logged user not found"));

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = currentUser.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        if (!isAdmin && !user.getEmail().equals(email)) {
            throw new RuntimeException("You can only deactivate your own account");
        }

        user.setIsActive(false);

        userRepository.save(user);
    }
}