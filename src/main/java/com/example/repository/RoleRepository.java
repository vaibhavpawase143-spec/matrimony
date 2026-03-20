package com.example.repository;

import com.example.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // 🔍 Get all active records
    List<Role> findByIsActiveTrue();

    // 🔍 Get all inactive records
    List<Role> findByIsActiveFalse();

    // 🔍 Filter by admin
    List<Role> findByAdminId(Long adminId);

    // 🔍 Search
    List<Role> findByNameContainingIgnoreCase(String keyword);
}