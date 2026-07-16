package com.example.serviceimpl;

import com.example.dto.request.UserFilterDTO;
import com.example.dto.response.AdminUserResponseDTO;
import com.example.dto.response.AdminUserStatsDTO;
import com.example.exception.ResourceNotFoundException;
import com.example.mapper.AdminUserMapper;
import com.example.model.Admin;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.AdminAuditLogService;
import com.example.service.AdminUserService;
import com.example.service.CurrentAdminService;
import com.example.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final AdminAuditLogService adminAuditLogService;

    private final CurrentAdminService currentAdminService;
    @Override
    @Transactional
    public Page<AdminUserResponseDTO> getAllUsers(
            UserFilterDTO filter,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return userRepository.findAll(
                UserSpecification.getUsers(filter),
                pageable
        ).map(AdminUserMapper::toDTO);
    }


    @Override
    @Transactional(readOnly = true)
    public AdminUserResponseDTO getUserById(Long id) {

        User user = userRepository
                .findByIdWithProfile(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return AdminUserMapper.toDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserResponseDTO> getActiveUsers() {

        return userRepository.findActiveUsersWithProfile()
                .stream()
                .map(AdminUserMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminUserResponseDTO> searchUsers(String keyword) {

        UserFilterDTO filter = new UserFilterDTO();
        filter.setSearch(keyword);

        return userRepository.findAll(UserSpecification.getUsers(filter))
                .stream()
                .map(AdminUserMapper::toDTO)
                .toList();
    }
    @Override
    public AdminUserResponseDTO activateUser(Long id) {

        User user = getUser(id);

        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        Admin admin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                admin.getId(),
                "USER_MANAGEMENT",
                "USER_ACTIVATED",
                "USER",
                savedUser.getId(),
                "Activated user: " + savedUser.getEmail(),
                "Active = false",
                "Active = true"
        );

        return AdminUserMapper.toDTO(savedUser);
    }
    @Override
    public AdminUserResponseDTO deactivateUser(Long id) {

        User user = getUser(id);

        user.setIsActive(false);

        User savedUser = userRepository.save(user);

        Admin admin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                admin.getId(),
                "USER_MANAGEMENT",
                "USER_DEACTIVATED",
                "USER",
                savedUser.getId(),
                "Deactivated user: " + savedUser.getEmail(),
                "Active = true",
                "Active = false"
        );

        return AdminUserMapper.toDTO(savedUser);
    }
    @Override
    public AdminUserResponseDTO verifyEmail(Long id) {

        User user = getUser(id);

        user.setEmailVerified(true);
        user.setEmailVerifiedAt(LocalDateTime.now());

        return AdminUserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public AdminUserResponseDTO verifyPhone(Long id) {

        User user = getUser(id);

        user.setPhoneVerified(true);
        user.setPhoneVerifiedAt(LocalDateTime.now());

        return AdminUserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public AdminUserResponseDTO blockUser(Long id) {

        User user = getUser(id);

        user.setIsBlocked(true);

        User savedUser = userRepository.save(user);

        Admin admin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                admin.getId(),
                "USER_MANAGEMENT",
                "USER_BLOCKED",
                "USER",
                savedUser.getId(),
                "Blocked user: " + savedUser.getEmail(),
                "Blocked = false",
                "Blocked = true"
        );

        return AdminUserMapper.toDTO(savedUser);
    }

    @Override
    public AdminUserResponseDTO unblockUser(Long id) {

        User user = getUser(id);

        user.setIsBlocked(false);

        User savedUser = userRepository.save(user);

        Admin admin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                admin.getId(),
                "USER_MANAGEMENT",
                "USER_UNBLOCKED",
                "USER",
                savedUser.getId(),
                "Unblocked user: " + savedUser.getEmail(),
                "Blocked = true",
                "Blocked = false"
        );

        return AdminUserMapper.toDTO(savedUser);
    }

    @Override
    public void softDeleteUser(Long id) {

        User user = getUser(id);

        user.setIsDeleted(true);
        user.setIsActive(false);

        User savedUser = userRepository.save(user);

        Admin admin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                admin.getId(),
                "USER_MANAGEMENT",
                "USER_SOFT_DELETED",
                "USER",
                savedUser.getId(),
                "Soft deleted user: " + savedUser.getEmail(),
                "Deleted = false",
                "Deleted = true"
        );
    }

    @Override
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }
    @Override
    @Transactional(readOnly = true)
    public AdminUserStatsDTO getUserStatistics() {

        Long totalUsers = userRepository.count();

        Long activeUsers = userRepository.countByIsActiveTrueAndIsDeletedFalse();

        Long blockedUsers = userRepository.countByIsBlockedTrueAndIsDeletedFalse();

        Long deletedUsers = userRepository.countByIsDeletedTrue();

        Long emailVerifiedUsers =
                userRepository.countByEmailVerifiedTrueAndIsDeletedFalse();

        Long phoneVerifiedUsers =
                userRepository.countByPhoneVerifiedTrueAndIsDeletedFalse();

        Long inactiveUsers = totalUsers - activeUsers;

        return new AdminUserStatsDTO(
                totalUsers,
                activeUsers,
                inactiveUsers,
                blockedUsers,
                deletedUsers,
                emailVerifiedUsers,
                phoneVerifiedUsers
        );
    }

    @Override
    public void bulkActivateUsers(List<Long> userIds) {

        List<User> users = getValidatedUsers(userIds);

        users.forEach(user -> user.setIsActive(true));

        userRepository.saveAll(users);

        Admin admin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                admin.getId(),
                "USER_MANAGEMENT",
                "BULK_USER_ACTIVATED",
                "USER",
                null,
                "Bulk activated " + users.size() + " users",
                null,
                null
        );
    }
    @Override

    public void bulkBlockUsers(List<Long> userIds) {

        List<User> users = getValidatedUsers(userIds);

        users.forEach(user -> user.setIsBlocked(true));

        userRepository.saveAll(users);

        Admin admin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                admin.getId(),
                "USER_MANAGEMENT",
                "BULK_USER_BLOCKED",
                "USER",
                null,
                "Bulk blocked " + users.size() + " users",
                null,
                null
        );
    }

    @Override
    public void bulkUnblockUsers(List<Long> userIds) {

        List<User> users = getValidatedUsers(userIds);

        users.forEach(user -> user.setIsBlocked(false));

        userRepository.saveAll(users);

        Admin admin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                admin.getId(),
                "USER_MANAGEMENT",
                "BULK_USER_UNBLOCKED",
                "USER",
                null,
                "Bulk unblocked " + users.size() + " users",
                null,
                null
        );
    }
    @Override
    public void bulkSoftDeleteUsers(List<Long> userIds) {

        List<User> users = getValidatedUsers(userIds);

        users.forEach(user -> {
            user.setIsDeleted(true);
            user.setIsActive(false);
        });

        userRepository.saveAll(users);

        Admin admin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                admin.getId(),
                "USER_MANAGEMENT",
                "BULK_USER_SOFT_DELETED",
                "USER",
                null,
                "Bulk soft deleted " + users.size() + " users",
                null,
                null
        );
    }
    @Override
    public AdminUserResponseDTO restoreUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        user.setIsDeleted(false);
        user.setIsActive(true);

        User savedUser = userRepository.save(user);

        Admin admin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                admin.getId(),
                "USER_MANAGEMENT",
                "USER_RESTORED",
                "USER",
                savedUser.getId(),
                "Restored user: " + savedUser.getEmail(),
                "Deleted = true",
                "Deleted = false"
        );

        return AdminUserMapper.toDTO(savedUser);
    }
    @Override
    @Transactional
    public byte[] exportUsersToCsv() {

        List<User> users = userRepository.findAllUsersWithProfile();

        StringBuilder csv = new StringBuilder();

        // Header
        csv.append("ID,Full Name,Email,Phone,Gender,Religion,City,Active,Blocked,Deleted,Email Verified,Phone Verified,Created At\n");

        // Data
        for (User user : users) {

            csv.append(user.getId()).append(",");

            csv.append(user.getFirstName() == null ? "" : user.getFirstName())
                    .append(" ")
                    .append(user.getLastName() == null ? "" : user.getLastName())
                    .append(",");

            csv.append(user.getEmail() == null ? "" : user.getEmail()).append(",");
            csv.append(user.getPhone() == null ? "" : user.getPhone()).append(",");

            csv.append(user.getProfile() != null && user.getProfile().getGender() != null
                    ? user.getProfile().getGender().getName()
                    : "").append(",");

            csv.append(user.getProfile() != null && user.getProfile().getReligion() != null
                    ? user.getProfile().getReligion().getName()
                    : "").append(",");

            csv.append(user.getProfile() != null && user.getProfile().getCity() != null
                    ? user.getProfile().getCity().getName()
                    : "").append(",");

            csv.append(user.getIsActive()).append(",");
            csv.append(user.getIsBlocked()).append(",");
            csv.append(user.getIsDeleted()).append(",");
            csv.append(user.getEmailVerified()).append(",");
            csv.append(user.getPhoneVerified()).append(",");
            csv.append(user.getCreatedAt());

            csv.append("\n");
        }
        Admin admin = currentAdminService.getCurrentAdmin();

        adminAuditLogService.log(
                admin.getId(),
                "USER_MANAGEMENT",
                "CSV_EXPORTED",
                "USER",
                null,
                "Exported " + users.size() + " users to CSV",
                null,
                null
        );
        return csv.toString().getBytes();
    }
    private List<User> getValidatedUsers(List<Long> userIds) {

        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException("User IDs cannot be empty");
        }

        List<User> users = userRepository.findAllById(userIds);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }

        if (users.size() != userIds.size()) {
            throw new ResourceNotFoundException("One or more users not found");
        }

        return users;
    }
    private User getUser(Long id) {

        return userRepository.findByIdWithProfile(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }
    @Override
    @Transactional
    public byte[] exportUsersToExcel() {

        List<User> users = userRepository.findAllUsersWithProfile();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Users");

            // Header Row
            Row headerRow = sheet.createRow(0);

            String[] headers = {
                    "ID",
                    "Full Name",
                    "Email",
                    "Phone",
                    "Gender",
                    "Religion",
                    "City",
                    "Active",
                    "Blocked",
                    "Deleted",
                    "Email Verified",
                    "Phone Verified",
                    "Created At"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;

            for (User user : users) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(user.getId());

                row.createCell(1).setCellValue(
                        (user.getFirstName() == null ? "" : user.getFirstName()) +
                                " " +
                                (user.getLastName() == null ? "" : user.getLastName())
                );

                row.createCell(2).setCellValue(user.getEmail() == null ? "" : user.getEmail());

                row.createCell(3).setCellValue(user.getPhone() == null ? "" : user.getPhone());

                row.createCell(4).setCellValue(
                        user.getProfile() != null &&
                                user.getProfile().getGender() != null
                                ? user.getProfile().getGender().getName()
                                : ""
                );

                row.createCell(5).setCellValue(
                        user.getProfile() != null &&
                                user.getProfile().getReligion() != null
                                ? user.getProfile().getReligion().getName()
                                : ""
                );

                row.createCell(6).setCellValue(
                        user.getProfile() != null &&
                                user.getProfile().getCity() != null
                                ? user.getProfile().getCity().getName()
                                : ""
                );

                row.createCell(7).setCellValue(Boolean.TRUE.equals(user.getIsActive()));

                row.createCell(8).setCellValue(Boolean.TRUE.equals(user.getIsBlocked()));

                row.createCell(9).setCellValue(Boolean.TRUE.equals(user.getIsDeleted()));

                row.createCell(10).setCellValue(Boolean.TRUE.equals(user.getEmailVerified()));

                row.createCell(11).setCellValue(Boolean.TRUE.equals(user.getPhoneVerified()));

                row.createCell(12).setCellValue(
                        user.getCreatedAt() == null ? "" : user.getCreatedAt().toString()
                );
            }

            // Auto Size Columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            Admin admin = currentAdminService.getCurrentAdmin();

            adminAuditLogService.log(
                    admin.getId(),
                    "USER_MANAGEMENT",
                    "EXCEL_EXPORTED",
                    "USER",
                    null,
                    "Exported " + users.size() + " users to Excel",
                    null,
                    null
            );
            return outputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to export users to Excel", e);
        }
    }
}