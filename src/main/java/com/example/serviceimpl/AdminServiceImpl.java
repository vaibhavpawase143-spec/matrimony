package com.example.serviceimpl;

import com.example.dto.response.AdminResponseDTO;
import com.example.mapper.AdminMapper;
import com.example.model.Admin;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.service.AdminService;
import lombok.RequiredArgsConstructor;
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

        return adminRepository.save(admin);
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

        return adminRepository.save(existing);
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

        targetAdmin.setIsActive(false);
        targetAdmin.setDeletedBy(currentAdmin.getId());
        targetAdmin.setDeletedAt(LocalDateTime.now());

        adminRepository.save(targetAdmin);
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