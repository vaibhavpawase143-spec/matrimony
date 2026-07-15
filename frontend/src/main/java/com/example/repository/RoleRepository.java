package com.example.repository;

import com.example.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // ✅ GLOBAL ROLE (IMPORTANT)
    Optional<Role> findByNameIgnoreCase(String name);

    // 🔽 Keep if you need admin-specific roles later
    Optional<Role> findByNameIgnoreCaseAndAdminId(String name, Long adminId);

    boolean existsByNameIgnoreCaseAndAdminId(String name, Long adminId);

    List<Role> findByAdminId(Long adminId);

    List<Role> findByAdminIdAndIsActiveTrue(Long adminId);

    List<Role> findByAdminIdAndIsActiveFalse(Long adminId);

    List<Role> findByAdminIdAndNameContainingIgnoreCase(Long adminId, String keyword);

    Optional<Role> findByName(String name);
}