package com.example.repository;

import com.example.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // =====================================================
    // JWT / SPRING SECURITY
    // =====================================================

    Optional<Role> findByName(String name);

    Optional<Role> findByNameIgnoreCase(String name);

    // =====================================================
    // BASIC
    // =====================================================

    Optional<Role> findByIdAndDeletedAtIsNull(Long id);

    Optional<Role> findByIdAndDeletedAtIsNotNull(Long id);

    List<Role> findAllByDeletedAtIsNull();

    List<Role> findByDeletedAtIsNotNull();

    // =====================================================
    // DUPLICATE CHECK
    // =====================================================

    boolean existsByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    Optional<Role> findByNameIgnoreCaseAndAdmin_IdAndDeletedAtIsNull(
            String name,
            Long adminId
    );

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<Role> findByIsActiveTrueAndDeletedAtIsNull();

    List<Role> findByIsActiveFalseAndDeletedAtIsNull();

    // =====================================================
    // ADMIN
    // =====================================================

    List<Role> findByAdmin_IdAndDeletedAtIsNull(Long adminId);

    List<Role> findByAdmin_IdAndIsActiveTrueAndDeletedAtIsNull(Long adminId);

    List<Role> findByAdmin_IdAndIsActiveFalseAndDeletedAtIsNull(Long adminId);

    // =====================================================
    // SEARCH
    // =====================================================

    List<Role> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );

    List<Role> findByAdmin_IdAndNameContainingIgnoreCaseAndDeletedAtIsNull(
            Long adminId,
            String keyword
    );
}