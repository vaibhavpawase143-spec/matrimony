package com.example.serviceimpl;

import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.PageResponse;
import com.example.dto.response.UserResponseDTO;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // ================= CURRENT USER =================
    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // ================= REGISTER =================
    @Override
    public User register(UserRegisterRequestDTO dto) {

        if (userRepository.existsByEmailIgnoreCaseOrPhone(dto.getEmail(), dto.getPhone())) {
            throw new RuntimeException("Email or phone already exists");
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setIsActive(true);

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.addRole(role);

        return userRepository.save(user);
    }

    // ================= LOGIN =================
    @Override
    public User login(String email, String password) {

        User user = userRepository.findByEmailIgnoreCaseAndIsActiveTrue(email)
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    @Override
    public String loginAndGenerateToken(String email, String password) {
        // handled in controller (JWT)
        return "Handled in controller";
    }

    // ================= GET BY ID =================
    @Override
    public Optional<UserResponseDTO> getById(Long id) {

        User targetUser = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User currentUser = getCurrentUser();

        boolean isSelf = targetUser.getId().equals(currentUser.getId());

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        if (!isSelf && !isAdmin) {
            throw new RuntimeException("Access Denied");
        }

        return Optional.of(mapToDTO(targetUser));
    }

    // ================= GET ALL =================
    @Override
    public List<UserResponseDTO> getAll() {

        User currentUser = getCurrentUser();

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new RuntimeException("Access Denied");
        }

        return userRepository.findAllWithRoles()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ================= ACTIVE USERS =================
    @Override
    public List<UserResponseDTO> getActiveUsers() {
        return userRepository.findActiveUsersWithRoles()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ================= SEARCH =================
    @Override
    public List<User> search(String keyword) {
        return userRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        keyword, keyword, keyword
                );
    }

    // ================= UPDATE =================
    @Override
    public User update(Long id, User requestUser) {

        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User currentUser = getCurrentUser();

        boolean isSelf = targetUser.getId().equals(currentUser.getId());

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        if (!isSelf && !isAdmin) {
            throw new RuntimeException("Access Denied");
        }

        targetUser.setFirstName(requestUser.getFirstName());
        targetUser.setLastName(requestUser.getLastName());
        targetUser.setPhone(requestUser.getPhone());

        if (requestUser.getPassword() != null && !requestUser.getPassword().isBlank()) {
            targetUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        }

        return userRepository.save(targetUser);
    }

    // ================= DELETE =================
    @Override
    public void deactivate(Long id) {

        User targetUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User currentUser = getCurrentUser();

        boolean isSelf = targetUser.getId().equals(currentUser.getId());

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(r -> r.getName().equals("ROLE_ADMIN"));

        if (!isSelf && !isAdmin) {
            throw new RuntimeException("Access Denied");
        }

        targetUser.setIsActive(false);

        userRepository.save(targetUser);
    }

    // ================= PAGINATION =================
    @Override
    public PageResponse<UserResponseDTO> getAllUsers(int page, int size) {
        return getAllUsers(page, size, "id", "asc");
    }

    @Override
    public PageResponse<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserResponseDTO> content = userPage.getContent()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
    }

    // ================= DTO =================
    private UserResponseDTO mapToDTO(User user) {

        UserResponseDTO dto = new UserResponseDTO();

        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setIsActive(user.getIsActive());
        dto.setEmailVerified(user.getEmailVerifiedAt() != null);
        dto.setPhoneVerified(user.getPhoneVerifiedAt() != null);

        dto.setRoles(
                user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet())
        );

        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }
}