package com.example.controller.admin;

import com.example.dto.request.BulkUserActionRequestDTO;
import com.example.dto.request.UserFilterDTO;
import com.example.dto.response.AdminUserResponseDTO;
import com.example.dto.response.AdminUserStatsDTO;
import com.example.dto.response.ApiResponse;
import com.example.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminUserManagementController {

    private final AdminUserService adminUserService;


    // ================= GET USERS =================
    @GetMapping
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ApiResponse<Page<AdminUserResponseDTO>> getAllUsers(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "createdAt") String sortBy,

            @RequestParam(defaultValue = "DESC") String direction,

            @RequestParam(required = false) String search,

            @RequestParam(required = false) Boolean active,

            @RequestParam(required = false) Boolean deleted,

            @RequestParam(required = false) Boolean blocked,

            @RequestParam(required = false) Boolean emailVerified,

            @RequestParam(required = false) Boolean phoneVerified,
            @RequestParam(required = false) Long genderId,
            @RequestParam(required = false) Long religionId,
            @RequestParam(required = false) Long casteId,
            @RequestParam(required = false) Long subCasteId,
            @RequestParam(required = false) Long countryId,
            @RequestParam(required = false) Long stateId,
            @RequestParam(required = false) Long cityId,
            @RequestParam(required = false) Long maritalStatusId,
            @RequestParam(required = false) Long educationLevelId,
            @RequestParam(required = false) Long occupationId,
            @RequestParam(required = false) Boolean isPremium,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) LocalDate registeredFrom,
            @RequestParam(required = false) LocalDate registeredTo,

            @RequestParam(required = false) String role
    ) {

        UserFilterDTO filter = new UserFilterDTO();

        filter.setSearch(search);
        filter.setIsActive(active);
        filter.setIsDeleted(deleted);
        filter.setIsBlocked(blocked);
        filter.setEmailVerified(emailVerified);
        filter.setPhoneVerified(phoneVerified);
        filter.setRole(role);
        filter.setGenderId(genderId);
        filter.setReligionId(religionId);
        filter.setCasteId(casteId);
        filter.setSubCasteId(subCasteId);
        filter.setCountryId(countryId);
        filter.setStateId(stateId);
        filter.setCityId(cityId);
        filter.setMaritalStatusId(maritalStatusId);
        filter.setEducationLevelId(educationLevelId);
        filter.setOccupationId(occupationId);
        filter.setIsPremium(isPremium);
        filter.setMinAge(minAge);
        filter.setMaxAge(maxAge);
        filter.setRegisteredFrom(registeredFrom);
        filter.setRegisteredTo(registeredTo);
        return new ApiResponse<>(
                true,
                "All users retrieved",
                adminUserService.getAllUsers(
                        filter,
                        page,
                        size,
                        sortBy,
                        direction
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ApiResponse<AdminUserResponseDTO> getUserById(
            @PathVariable Long id
    ) {

        return new ApiResponse<>(
                true,
                "User retrieved",
                adminUserService.getUserById(id)
        );
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ApiResponse<List<AdminUserResponseDTO>> getActiveUsers() {

        return new ApiResponse<>(
                true,
                "Active users retrieved",
                adminUserService.getActiveUsers()
        );
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ApiResponse<List<AdminUserResponseDTO>> searchUsers(
            @RequestParam String keyword
    ) {

        return new ApiResponse<>(
                true,
                "Users found",
                adminUserService.searchUsers(keyword)
        );
    }

    // ================= USER MANAGEMENT =================

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    public ApiResponse<AdminUserResponseDTO> activateUser(
            @PathVariable Long id
    ) {

        return new ApiResponse<>(
                true,
                "User activated successfully",
                adminUserService.activateUser(id)
        );
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    public ApiResponse<AdminUserResponseDTO> deactivateUser(
            @PathVariable Long id
    ) {

        return new ApiResponse<>(
                true,
                "User deactivated successfully",
                adminUserService.deactivateUser(id)
        );
    }

    @PutMapping("/{id}/verify-email")
    @PreAuthorize("hasAuthority('USER_VERIFY')")
    public ApiResponse<AdminUserResponseDTO> verifyUserEmail(
            @PathVariable Long id
    ) {

        return new ApiResponse<>(
                true,
                "Email verified successfully",
                adminUserService.verifyEmail(id)
        );
    }

    @PutMapping("/{id}/verify-phone")
    @PreAuthorize("hasAuthority('USER_VERIFY')")
    public ApiResponse<AdminUserResponseDTO> verifyUserPhone(
            @PathVariable Long id
    ) {

        return new ApiResponse<>(
                true,
                "Phone verified successfully",
                adminUserService.verifyPhone(id)
        );
    }

    @PutMapping("/{id}/block")
    @PreAuthorize("hasAuthority('USER_BLOCK')")
    public ApiResponse<AdminUserResponseDTO> blockUser(
            @PathVariable Long id
    ) {

        return new ApiResponse<>(
                true,
                "User blocked successfully",
                adminUserService.blockUser(id)
        );
    }

    @PutMapping("/{id}/unblock")
    @PreAuthorize("hasAuthority('USER_BLOCK')")
    public ApiResponse<AdminUserResponseDTO> unblockUser(
            @PathVariable Long id
    ) {

        return new ApiResponse<>(
                true,
                "User unblocked successfully",
                adminUserService.unblockUser(id)
        );
    }

    // ================= SUPER ADMIN ONLY =================

    @PutMapping("/{id}/soft-delete")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ApiResponse<String> softDeleteUser(
            @PathVariable Long id
    ) {

        adminUserService.softDeleteUser(id);

        return new ApiResponse<>(
                true,
                "User soft deleted successfully",
                null
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ApiResponse<String> deleteUser(
            @PathVariable Long id
    ) {

        adminUserService.deleteUser(id);

        return new ApiResponse<>(
                true,
                "User deleted successfully",
                null
        );
    }

    @GetMapping("/export")
    @PreAuthorize("hasAuthority('REPORT_EXPORT')")
    public ResponseEntity<String> exportUsers() {

        return ResponseEntity.ok("Export functionality coming soon");
    }

    // ================= STATISTICS =================

    @GetMapping("/stats")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ApiResponse<AdminUserStatsDTO> getUserStatistics() {

        return new ApiResponse<>(
                true,
                "User statistics retrieved successfully",
                adminUserService.getUserStatistics()
        );
    }
    @PutMapping("/bulk/activate")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    public ApiResponse<String> bulkActivateUsers(
            @Valid @RequestBody BulkUserActionRequestDTO request
    ) {

        adminUserService.bulkActivateUsers(request.getUserIds());

        return new ApiResponse<>(
                true,
                request.getUserIds().size() + " users activated successfully",
                null
        );
    }
    @PutMapping("/bulk/block")
    @PreAuthorize("hasAuthority('USER_BLOCK')")
    public ApiResponse<String> bulkBlockUsers(
            @Valid @RequestBody BulkUserActionRequestDTO request
    ) {

        adminUserService.bulkBlockUsers(request.getUserIds());

        return new ApiResponse<>(
                true,
                request.getUserIds().size() + " users blocked successfully",
                null
        );
    }
    @PutMapping("/bulk/unblock")
    @PreAuthorize("hasAuthority('USER_BLOCK')")
    public ApiResponse<String> bulkUnblockUsers(
            @Valid @RequestBody BulkUserActionRequestDTO request
    ) {

        adminUserService.bulkUnblockUsers(request.getUserIds());

        return new ApiResponse<>(
                true,
                request.getUserIds().size() + " users unblocked successfully",
                null
        );
    }
    @PutMapping("/bulk/soft-delete")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ApiResponse<String> bulkSoftDeleteUsers(
            @Valid @RequestBody BulkUserActionRequestDTO request
    ) {

        adminUserService.bulkSoftDeleteUsers(request.getUserIds());

        return new ApiResponse<>(
                true,
                request.getUserIds().size() + " users soft deleted successfully",
                null
        );
    }
    @PutMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    public ApiResponse<AdminUserResponseDTO> restoreUser(
            @PathVariable Long id
    ) {

        return new ApiResponse<>(
                true,
                "User restored successfully",
                adminUserService.restoreUser(id)
        );
    }
    @GetMapping("/export/csv")
    @PreAuthorize("hasAuthority('REPORT_EXPORT')")
    public ResponseEntity<byte[]> exportUsersToCsv() {

        byte[] csvData = adminUserService.exportUsersToCsv();

        return ResponseEntity.ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=users.csv"
                )
                .header(
                        "Content-Type",
                        "text/csv"
                )
                .body(csvData);
    }
    @GetMapping("/export/excel")
    @PreAuthorize("hasAuthority('REPORT_EXPORT')")
    public ResponseEntity<byte[]> exportUsersToExcel() {

        byte[] excelData = adminUserService.exportUsersToExcel();

        return ResponseEntity.ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=users.xlsx"
                )
                .header(
                        "Content-Type",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
                .body(excelData);
    }
}