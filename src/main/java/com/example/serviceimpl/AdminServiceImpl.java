package com.example.serviceimpl;

import com.example.dto.request.AdminFilterDTO;
import com.example.dto.request.AdminResetPasswordDTO;
import com.example.dto.request.AdminUpdateDTO;
import com.example.dto.response.AdminResponseDTO;
import com.example.dto.response.AdminStatsDTO;
import com.example.mapper.AdminMapper;
import com.example.model.Admin;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.service.AdminAuditLogService;
import com.example.service.AdminService;
import com.example.specification.AdminSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AdminAuditLogService auditLogService;
    // ================= CURRENT ADMIN =================
    private Admin getCurrentAdmin() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return adminRepository.findByEmailWithRole(email)
                .orElseThrow(() -> new RuntimeException("Logged-in admin not found"));
    }

    // ================= REGISTER =================
    @Override
    public Admin register(Admin admin) {

        if (adminRepository.existsByEmail(admin.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        Role role = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        admin.setRole(role);
        admin.setIsActive(true);

        Admin savedAdmin = adminRepository.save(admin);

        auditLogService.log(
                getCurrentAdmin().getId(),
                "ADMIN_MANAGEMENT",
                "ADMIN_CREATED",
                "ADMIN",
                savedAdmin.getId(),
                "Created admin: " + savedAdmin.getName(),
                null,
                "Role=" + savedAdmin.getRole().getName()
        );

        return savedAdmin;
    }

    @Override
    public Admin create(Admin admin) {
        return register(admin);
    }

    // ================= GET BY ID (DTO) =================
    @Override
    public AdminResponseDTO getById(Long id) {

        Admin admin = adminRepository.findByIdWithRole(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        return AdminMapper.toDTO(admin);
    }

    // ================= GET ALL (DTO) =================
    @Override
    public List<AdminResponseDTO> getAll() {

        return adminRepository.findAllWithRole()
                .stream()
                .map(AdminMapper::toDTO)
                .toList();
    }

    // ================= UPDATE =================
    @Override
    public Admin update(Long id, Admin updatedAdmin) {

        Admin existing = adminRepository.findByIdWithRole(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!existing.getEmail().equalsIgnoreCase(email)) {
            throw new RuntimeException("You can only update your own account");
        }

        existing.setName(updatedAdmin.getName());
        existing.setPhone(updatedAdmin.getPhone());

        if (updatedAdmin.getPassword() != null && !updatedAdmin.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(updatedAdmin.getPassword()));
        }

        Admin saved = adminRepository.save(existing);

        return adminRepository.findByIdWithRole(saved.getId())
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    // ================= DELETE =================
    @Override
    public void delete(Long id) {

        Admin currentAdmin = getCurrentAdmin();
        Admin targetAdmin = adminRepository.findByIdWithRole(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!currentAdmin.getRole().getName().equals("ROLE_SUPER_ADMIN")) {
            throw new RuntimeException("Only SUPER ADMIN can delete admin");
        }

        if (currentAdmin.getId().equals(id)) {
            throw new RuntimeException("You cannot delete yourself");
        }
// Prevent deleting the last active Super Admin
        if ("ROLE_SUPER_ADMIN".equals(targetAdmin.getRole().getName())
                && adminRepository.countActiveSuperAdmins() <= 1) {

            throw new RuntimeException(
                    "Cannot delete the last active Super Admin."
            );
        }
        targetAdmin.setIsActive(false);
        targetAdmin.setDeletedBy(currentAdmin.getId());
        targetAdmin.setDeletedAt(LocalDateTime.now());

        adminRepository.save(targetAdmin);
        auditLogService.log(
                currentAdmin.getId(),
                "ADMIN_MANAGEMENT",
                "ADMIN_DELETED",
                "ADMIN",
                targetAdmin.getId(),
                "Deactivated admin account: " + targetAdmin.getName(),
                "Active=true",
                "Active=false"
        );
    }

    // ================= LOGIN =================
    @Override
    @Transactional
    public Admin login(String email, String password) {

        Admin admin = adminRepository.findByEmailWithRole(email)
                .orElseThrow(() -> new RuntimeException("Invalid email"));

        if (!admin.getIsActive()) {
            throw new RuntimeException("Admin is inactive");
        }

        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // 🔥 ensure role loaded
        admin.getRole().getName();

        admin.setLastLogin(LocalDateTime.now());

        return adminRepository.save(admin);
    }

    @Override
    public Admin findByEmail(String email) {
        return adminRepository.findByEmailWithRole(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AdminResponseDTO> getAllAdmins(AdminFilterDTO filter) {

        Sort sort = Sort.by(
                Sort.Direction.fromString(filter.getDirection()),
                filter.getSortBy()
        );

        Pageable pageable = PageRequest.of(
                filter.getPage(),
                filter.getSize(),
                sort
        );

        Specification<Admin> specification =
                AdminSpecification.filter(filter);

        Page<Admin> adminPage =
                adminRepository.findAll(specification, pageable);

        return adminPage.map(AdminMapper::toDTO);
    }

    @Override
    @Transactional
    public AdminResponseDTO updateAdmin(Long id, AdminUpdateDTO dto) {

        Admin admin = adminRepository.findByIdWithRole(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        // Update basic fields
        admin.setName(dto.getName());
        admin.setPhone(dto.getPhone());
        admin.setProfilePhoto(dto.getProfilePhoto());

        // Update Active Status
        if (dto.getIsActive() != null) {
            admin.setIsActive(dto.getIsActive());
        }

        // Update Role (Optional)
        if (dto.getRoleId() != null) {

            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found"));

            // Prevent changing the last active Super Admin
            if ("ROLE_SUPER_ADMIN".equals(admin.getRole().getName())
                    && !"ROLE_SUPER_ADMIN".equals(role.getName())
                    && adminRepository.countActiveSuperAdmins() <= 1) {

                throw new RuntimeException(
                        "Cannot change the role of the last active Super Admin."
                );
            }
            Admin currentAdmin = getCurrentAdmin();

            if (currentAdmin.getId().equals(admin.getId())
                    && !currentAdmin.getRole().getName().equals(role.getName())) {

                throw new RuntimeException(
                        "You cannot change your own role."
                );
            }
            admin.setRole(role);
        }

        Admin updatedAdmin = adminRepository.save(admin);
        auditLogService.log(
                getCurrentAdmin().getId(),
                "ADMIN_MANAGEMENT",
                "ADMIN_UPDATED",
                "ADMIN",
                updatedAdmin.getId(),
                "Updated admin: " + updatedAdmin.getName(),
                null,
                "Name=" + updatedAdmin.getName()
                        + ", Phone=" + updatedAdmin.getPhone()
                        + ", Role=" + updatedAdmin.getRole().getName()
        );
        return AdminMapper.toDTO(updatedAdmin);
    }

    @Override
    @Transactional
    public void activateAdmin(Long id) {

        Admin admin = adminRepository.findByIdWithRole(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (Boolean.TRUE.equals(admin.getIsActive())) {
            throw new RuntimeException("Admin is already active");
        }

        admin.setIsActive(true);
        admin.setDeletedAt(null);
        admin.setDeletedBy(null);

        adminRepository.save(admin);
        auditLogService.log(
                getCurrentAdmin().getId(),
                "ADMIN_MANAGEMENT",
                "ADMIN_ACTIVATED",
                "ADMIN",
                admin.getId(),
                "Activated admin: " + admin.getName(),
                "Active=false",
                "Active=true"
        );
    }

    @Override
    @Transactional
    public void deactivateAdmin(Long id) {

        Admin currentAdmin = getCurrentAdmin();

        Admin admin = adminRepository.findByIdWithRole(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!Boolean.TRUE.equals(admin.getIsActive())) {
            throw new RuntimeException("Admin is already inactive");
        }

        if (currentAdmin.getId().equals(admin.getId())) {
            throw new RuntimeException("You cannot deactivate your own account");
        }
// Prevent deactivating the last active Super Admin
        if ("ROLE_SUPER_ADMIN".equals(admin.getRole().getName())
                && adminRepository.countActiveSuperAdmins() <= 1) {

            throw new RuntimeException(
                    "Cannot deactivate the last active Super Admin."
            );
        }
        // Prevent deleting the last active Super Admin

        admin.setIsActive(false);
        admin.setDeletedBy(currentAdmin.getId());
        admin.setDeletedAt(LocalDateTime.now());

        adminRepository.save(admin);
        auditLogService.log(
                currentAdmin.getId(),
                "ADMIN_MANAGEMENT",
                "ADMIN_DEACTIVATED",
                "ADMIN",
                admin.getId(),
                "Deactivated admin: " + admin.getName(),
                "Active=true",
                "Active=false"
        );
    }
    @Override
    @Transactional
    public void resetPassword(Long id, AdminResetPasswordDTO dto) {

        Admin admin = adminRepository.findByIdWithRole(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        admin.setPassword(
                passwordEncoder.encode(dto.getNewPassword())
        );

        adminRepository.save(admin);
        auditLogService.log(
                getCurrentAdmin().getId(),
                "ADMIN_MANAGEMENT",
                "PASSWORD_RESET",
                "ADMIN",
                admin.getId(),
                "Reset password for admin: " + admin.getName(),
                null,
                null
        );
    }

    @Override
    public AdminStatsDTO getStatistics() {

        long totalAdmins = adminRepository.count();

        long activeAdmins = adminRepository.countByIsActiveTrue();

        long inactiveAdmins = adminRepository.countByIsActiveFalse();

        long superAdmins = adminRepository.countByRole("ROLE_SUPER_ADMIN");

        long normalAdmins = adminRepository.countByRole("ROLE_ADMIN");

        long newAdminsThisMonth = adminRepository.countNewAdmins(
                LocalDateTime.now().withDayOfMonth(1)
        );

        return AdminStatsDTO.builder()
                .totalAdmins(totalAdmins)
                .activeAdmins(activeAdmins)
                .inactiveAdmins(inactiveAdmins)
                .superAdmins(superAdmins)
                .normalAdmins(normalAdmins)
                .newAdminsThisMonth(newAdminsThisMonth)
                .build();
    }

    @Override
    @Transactional
    public String blockUser(Long userId) {


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.getIsBlocked())) {
            return "User already blocked";
        }

        user.setIsBlocked(true);
        userRepository.save(user);

        return "User blocked successfully";
    }

    @Override
    @Transactional
    public String unblockUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Boolean.TRUE.equals(user.getIsBlocked())) {
            return "User is not blocked";
        }

        user.setIsBlocked(false);
        user.setReportCount(0);

        userRepository.save(user);

        return "User unblocked successfully";
    }


}