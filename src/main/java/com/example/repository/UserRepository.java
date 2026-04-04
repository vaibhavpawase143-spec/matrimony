package com.example.repository;

import com.example.model.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> { // ================= AUTH =================

    Optional<User> findByEmailIgnoreCaseAndIsActiveTrue(String email);

    Optional<User> findByEmailIgnoreCase(String email);

    // 🔥 REQUIRED FOR JWT LOGIN
    Optional<User> findByEmail(String email);

    // 🔥 JOIN FETCH (roles)
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmailWithRoles(@Param("email") String email);


    // ================= VALIDATION =================

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmail(String email);

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    boolean existsByEmailIgnoreCaseOrPhone(String email, String phone);


    // ================= BASIC =================

    List<User> findByIsActiveTrue();

    Optional<User> findByIdAndIsActiveTrue(Long id);


    // ================= SEARCH =================

    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName,
            String lastName,
            String email
    );


    // ================= ROLE FETCH =================

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles")
    List<User> findAllWithRoles();

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") Long id);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.isActive = true")
    List<User> findActiveUsersWithRoles();


    // ================= SIMPLE SEARCH =================

    List<User> findByEmailContainingIgnoreCase(String keyword);
}