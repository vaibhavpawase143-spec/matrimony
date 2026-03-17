package com.example.repository;

import com.example.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // 🔍 Find by name (USER, ADMIN)
    Optional<Role> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Filter by admin
    List<Role> findByAdminId(Long adminId);

    // 🔍 Search
    List<Role> findByNameContainingIgnoreCase(String keyword);
}