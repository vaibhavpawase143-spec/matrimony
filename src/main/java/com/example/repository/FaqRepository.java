package com.example.repository;

import com.example.model.Faq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FaqRepository extends JpaRepository<Faq, Long> {

    /**
     * Find FAQ by ID only if active
     */
    Optional<Faq> findByIdAndIsActiveTrue(Long id);

    /**
     * Public FAQs
     */
    List<Faq> findByPublishedTrueAndIsActiveTrueOrderByDisplayOrderAsc();

    /**
     * Active FAQs for Admin
     */
    List<Faq> findByIsActiveTrueOrderByDisplayOrderAsc();

    /**
     * Validation
     */
    boolean existsByQuestionIgnoreCase(String question);
}