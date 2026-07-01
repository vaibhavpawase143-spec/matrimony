package com.example.repository;

import com.example.model.CmsPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CmsPageRepository extends JpaRepository<CmsPage, Long> {

    /**
     * Find CMS page by page key
     */
    Optional<CmsPage> findByPageKey(String pageKey);

    /**
     * Find only published pages
     */
    List<CmsPage> findByPublishedTrue();

    /**
     * Find only active pages
     */
    List<CmsPage> findByIsActiveTrue();

    /**
     * Find published & active page
     */
    Optional<CmsPage> findByPageKeyAndPublishedTrueAndIsActiveTrue(
            String pageKey
    );

    /**
     * Validation
     */
    boolean existsByPageKey(String pageKey);
}