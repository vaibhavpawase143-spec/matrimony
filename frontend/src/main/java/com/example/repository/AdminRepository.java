package com.example.repository;

import com.example.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // ================= 🔐 LOGIN =================

    // Keep this (fallback)
    Optional<Admin> findByEmailIgnoreCase(String email);

    // ✅ MAIN METHOD (USE THIS FOR LOGIN)
    @Query("SELECT a FROM Admin a JOIN FETCH a.role WHERE upper(a.email) = upper(:email)")
    Optional<Admin> findByEmailWithRole(@Param("email") String email);


    // ================= 🔍 OPTIONAL =================

    Optional<Admin> findByUsername(String username);


    // ================= ✅ VALIDATION =================

    boolean existsByEmail(String email);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsername(String username);


    // ================= 🔥 FETCH HELPERS =================

    @Query("SELECT a FROM Admin a JOIN FETCH a.role WHERE a.id = :id")
    Optional<Admin> findByIdWithRole(@Param("id") Long id);

    @Query("SELECT a FROM Admin a JOIN FETCH a.role")
    List<Admin> findAllWithRole();
}