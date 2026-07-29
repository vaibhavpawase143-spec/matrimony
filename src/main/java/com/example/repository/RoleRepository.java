package com.example.repository;

import com.example.model.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    boolean existsByNameIgnoreCaseAndDeletedAtIsNull(String name);

    Optional<Role> findByNameIgnoreCaseAndDeletedAtIsNull(String name);

    // =====================================================
    // ACTIVE / INACTIVE
    // =====================================================

    List<Role> findByIsActiveTrueAndDeletedAtIsNull();

    List<Role> findByIsActiveFalseAndDeletedAtIsNull();

    // =====================================================
    // SEARCH
    // =====================================================

    List<Role> findByNameContainingIgnoreCaseAndDeletedAtIsNull(
            String keyword
    );
    // =====================================================
// RBAC
// =====================================================

    @EntityGraph(attributePaths = {"permissions"})
    @Query("""
       SELECT r
       FROM Role r
       WHERE r.name = :name
       AND r.deletedAt IS NULL
       """)
    Optional<Role> findByNameWithPermissions(@Param("name") String name);
}