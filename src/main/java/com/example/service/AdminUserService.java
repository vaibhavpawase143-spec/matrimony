package com.example.service;

import com.example.dto.request.UserFilterDTO;
import com.example.dto.response.AdminUserResponseDTO;
import com.example.dto.response.AdminUserStatsDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminUserService {

    // ================= USERS =================

    Page<AdminUserResponseDTO> getAllUsers(
            UserFilterDTO filter,
            int page,
            int size,
            String sortBy,
            String direction
    );

    AdminUserResponseDTO getUserById(Long id);

    List<AdminUserResponseDTO> getActiveUsers();

    List<AdminUserResponseDTO> searchUsers(String keyword);

    // ================= USER MANAGEMENT =================

    AdminUserResponseDTO activateUser(Long id);

    AdminUserResponseDTO deactivateUser(Long id);

    AdminUserResponseDTO verifyEmail(Long id);

    AdminUserResponseDTO verifyPhone(Long id);

    AdminUserResponseDTO blockUser(Long id);

    AdminUserResponseDTO unblockUser(Long id);

    void softDeleteUser(Long id);

    void deleteUser(Long id);

    // ================= DASHBOARD =================

    AdminUserStatsDTO getUserStatistics();
    void bulkActivateUsers(List<Long> userIds);
    void bulkBlockUsers(List<Long> userIds);
    void bulkUnblockUsers(List<Long> userIds);
    void bulkSoftDeleteUsers(List<Long> userIds);
    AdminUserResponseDTO restoreUser(Long id);
    byte[] exportUsersToCsv();
    byte[] exportUsersToExcel();
}