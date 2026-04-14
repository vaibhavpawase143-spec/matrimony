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

    /**
     * Register new user and send verification email
     */
    User register(UserRegisterRequestDTO request);

    /**
     * Validate login credentials
     */
    User login(String email, String password);

    /**
     * Login and generate JWT token
     */
    String loginAndGenerateToken(String email, String password);


    // ================= EMAIL VERIFICATION =================

    /**
     * Verify email using token
     */
    void verifyEmail(String token);

    /**
     * Resend verification email
     */
    void resendVerification(String email);

    /**
     * Send verification email manually
     */
    void sendVerification(String email);


    // ================= PASSWORD =================

    /**
     * Send forgot password email with reset token
     */
    void forgotPassword(String email);

    /**
     * Reset password using token
     */
    void resetPassword(String token, String newPassword);


    // ================= USER =================

    /**
     * Get user by ID (DTO)
     */
    Optional<UserResponseDTO> getById(Long id);

    /**
     * Get all users (ADMIN)
     */
    List<UserResponseDTO> getAll();

    /**
     * Get only active users
     */
    List<UserResponseDTO> getActiveUsers();

    /**
     * Update user details
     */
    User update(Long id, User updatedUser);

    /**
     * Delete user permanently
     */
    void deleteUser(Long id);

    /**
     * Deactivate user (soft delete)
     */
    void deactivate(Long id);


    // ================= SEARCH =================

    /**
     * Search users by name/email
     */
    List<UserResponseDTO> search(String keyword);


    // ================= PAGINATION =================

    /**
     * Get paginated users with filters
     */
    PageResponse<UserResponseDTO> getAllUsers(
            int page,
            int size,
            String sortBy,
            String direction,
            UserFilterDTO filter
    );


    // ================= INTERNAL =================

    /**
     * Find user by email (used in chat/socket/security)
     */
    Optional<User> findByEmail(String email);
}