package com.example.repository;

import com.example.model.UserDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository
        extends JpaRepository<UserDetails, Long>,
        JpaSpecificationExecutor<UserDetails> {

    // ✅ Correct method
    Optional<UserDetails> findByUserId(Long userId);
}