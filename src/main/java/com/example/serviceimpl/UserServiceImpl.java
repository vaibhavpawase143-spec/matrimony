package com.example.serviceimpl;

import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.PageResponse;
import com.example.dto.response.UserResponseDTO;
import com.example.mapper.UserMapper;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.security.JwtUtil;
import com.example.service.RoleService;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
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
    // REGISTER
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
    // LOGIN
    // =========================
    @Override
    public User login(String email, String password) {

        User user = userRepository
                .findByEmailIgnoreCaseAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    // =========================
    // JWT LOGIN
    // =========================
    @Override
    public String loginAndGenerateToken(String email, String password) {

        User user = userRepository
                .findByEmailIgnoreCaseAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
    // GET BY ID (DTO)
    // =========================
    @Override
    public Optional<UserResponseDTO> getById(Long id) {
        return userRepository.findByIdWithRoles(id)
                .map(UserMapper::toDTO);
    }

    // =========================
    // GET ALL (DTO)
    // =========================
    @Override
    public List<UserResponseDTO> getAll() {
        return userRepository.findAllWithRoles()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    // =========================
    // ACTIVE USERS (DTO)
    // =========================
    @Override
    public List<UserResponseDTO> getActiveUsers() {
        return userRepository.findActiveUsersWithRoles()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    // =========================
    // SEARCH (ENTITY)
    // =========================
    @Override
    public List<User> search(String keyword) {
        return userRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword
                );
    }

    // =========================
    // UPDATE
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
    // DEACTIVATE
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

    // =========================
    // PAGINATION (DTO FIXED)
    // =========================
    @Override
    public PageResponse<UserResponseDTO> getAllUsers(int page, int size) {

        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size));

        List<UserResponseDTO> content = userPage.getContent()
                .stream()
                .map(UserMapper::toDTO)
                .toList();

        return new PageResponse<>(
                content,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages()
        );
    }

    @Override
    public PageResponse<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String direction) {

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
                userPage.getTotalPages()
        );
    }
}