package com.example.repository;

import com.example.model.UserDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository
        extends JpaRepository<UserDetails, Long>,
        JpaSpecificationExecutor<UserDetails> {
}