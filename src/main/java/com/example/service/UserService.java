package com.example.service;

import com.example.dto.request.UserFilterDTO;
import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.PageResponse;
import com.example.dto.response.UserResponseDTO;
import com.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // ================= AUTH =================
    User register(UserRegisterRequestDTO request);

    User login(String email, String password);

    String loginAndGenerateToken(String email, String password);

    // ================= USER =================
    Optional<UserResponseDTO> getById(Long id);

    List<UserResponseDTO> getAll();

    List<UserResponseDTO> getActiveUsers();

    User update(Long id, User updatedUser);

    void deleteUser(Long id);

    void deactivate(Long id);

    // ================= SEARCH =================
    List<UserResponseDTO> search(String keyword);

    // ================= PAGINATION =================
    PageResponse<UserResponseDTO> getAllUsers(
            int page,
            int size,
            String sortBy,
            String direction,
            UserFilterDTO filter
    );

    // ================= OPTIONAL FEATURES =================
    void verifyEmail(String token);

    void resendVerification(String email);

    void forgotPassword(String email);

    void resetPassword(String token, String newPassword);

    void sendVerification(String email);

    // ================= 🔥 REQUIRED FOR CHAT =================
    Optional<User> findByEmail(String email);
}