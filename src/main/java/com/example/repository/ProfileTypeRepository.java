package com.example.repository;

import com.example.model.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileTypeRepository extends JpaRepository<ProfileType, Long> {

    // 🔍 Find by name (Public, Private, Premium)
    Optional<ProfileType> findByName(String name);

    // 🔍 Check duplicate
    boolean existsByName(String name);

    // 🔍 Filter by admin
    List<ProfileType> findByAdminId(Long adminId);

    // 🔍 Search
    List<ProfileType> findByNameContainingIgnoreCase(String keyword);
}