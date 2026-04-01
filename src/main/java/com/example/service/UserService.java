package com.example.service;

import com.example.dto.request.UserRegisterRequestDTO;
import com.example.dto.response.PageResponse;
import com.example.dto.response.UserResponseDTO;
import com.example.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // AUTH
    User register(UserRegisterRequestDTO request);

    User login(String email, String password);

    String loginAndGenerateToken(String email, String password);

    // EMAIL
    void verifyEmail(String token);

    void resendVerification(String email);

    // PASSWORD
    void forgotPassword(String email);

    void resetPassword(String token, String newPassword);

    // USER
    Optional<UserResponseDTO> getById(Long id);

    List<UserResponseDTO> getAll();

    List<UserResponseDTO> getActiveUsers();

    List<User> search(String keyword);

    User update(Long id, User user);

    void deactivate(Long id);

    // PAGINATION
    PageResponse<UserResponseDTO> getAllUsers(int page, int size);

    PageResponse<UserResponseDTO> getAllUsers(int page, int size, String sortBy, String direction);
}