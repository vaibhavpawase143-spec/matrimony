package com.example.repository;

import com.example.model.SuccessStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuccessStoryRepository extends JpaRepository<SuccessStory, Long> {

    Page<SuccessStory> findByIsPublishedTrueAndConsentGivenTrue(Pageable pageable);

    @Query("""
        SELECT s FROM SuccessStory s
        WHERE s.isPublished = true AND s.consentGiven = true
        ORDER BY COALESCE(s.publishedAt, s.updatedAt, s.createdAt) DESC, s.id DESC
    """)
    Page<SuccessStory> findPublicStoriesOrderedByRecency(Pageable pageable);

    Optional<SuccessStory> findByIdAndIsPublishedTrueAndConsentGivenTrue(Long id);

    @Query("""
        SELECT s FROM SuccessStory s
        WHERE (:search IS NULL OR :search = ''
           OR LOWER(s.partnerOneName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(s.partnerTwoName) LIKE LOWER(CONCAT('%', :search, '%'))
           OR LOWER(s.location) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:published IS NULL OR s.isPublished = :published)
    """)
    Page<SuccessStory> searchAdminStories(
            @Param("search") String search,
            @Param("published") Boolean published,
            Pageable pageable
    );
}
