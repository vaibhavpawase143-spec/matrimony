package com.example.serviceimpl;

import com.example.model.Role;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.JwtUtil;
import com.example.service.RoleService;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    // =========================
    // ✅ REGISTER
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

        // 🔥 Assign default role
        Role role = roleService.getByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));

        user.setRole(role);

        user.setIsActive(true);

        return userRepository.save(user);
    }

    // =========================
    // 🔐 LOGIN (OPTIONAL)
    // =========================
    @Override
    public User login(String email, String password) {

        User user = userRepository
                .findByEmailIgnoreCaseAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("User not found or inactive"));

        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    // =========================
    // 🔥 JWT LOGIN (ADVANCED)
    // =========================
    @Override
    public String loginAndGenerateToken(String email, String password) {

        User user = userRepository
                .findByEmailIgnoreCaseAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("User not found or inactive"));

        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 🔥 ADD ROLES IN JWT
        List<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName())
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
    // ✅ UPDATE
    // =========================
    @Override
    public User update(Long id, User updatedUser) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setPhone(updatedUser.getPhone());

        return userRepository.save(user);
    }

    // =========================
    // ❌ DEACTIVATE
    // =========================
    @Override
    public void deactivate(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setIsActive(false);

        userRepository.save(user);
    }
}