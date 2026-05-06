package com.example.repository;

import com.example.model.SystemConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, Long> {

    // Find by config key
    Optional<SystemConfiguration> findByConfigKey(String configKey);

    // Find by category
    List<SystemConfiguration> findByCategory(String category);

    // Check if active
    List<SystemConfiguration> findByIsActiveTrue();
}

