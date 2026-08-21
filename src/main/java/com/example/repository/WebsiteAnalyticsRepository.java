package com.example.repository;

import com.example.model.WebsiteAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WebsiteAnalyticsRepository
        extends JpaRepository<WebsiteAnalytics, Long> {

    @Modifying
    @Query("""
        UPDATE WebsiteAnalytics w
        SET w.profileHits = w.profileHits + 1
        WHERE w.id = 1
    """)
    int incrementProfileHits();
}