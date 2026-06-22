package com.example.repository;

import com.example.model.PhotoType;
import com.example.model.UserPhoto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

    // =========================
    // USER PHOTOS
    // =========================

    List<UserPhoto> findByUserId(Long userId);

    long countByUserId(Long userId);

    // =========================
    // PHOTO TYPE
    // =========================

    List<UserPhoto> findByUserIdAndPhotoType(
            Long userId,
            PhotoType photoType
    );

    Optional<UserPhoto> findFirstByUserIdAndPhotoType(
            Long userId,
            PhotoType photoType
    );

    boolean existsByUserIdAndPhotoType(
            Long userId,
            PhotoType photoType
    );

    void deleteByUserIdAndPhotoType(
            Long userId,
            PhotoType photoType
    );

    // =========================
    // PRIMARY PHOTO
    // =========================

    Optional<UserPhoto> findFirstByUserIdAndPrimaryPhotoTrue(
            Long userId
    );
    Optional<UserPhoto> findFirstByUserIdOrderByCreatedAtAsc(
            Long userId
    );
    @Modifying
    @Query("""
           update UserPhoto p
           set p.primaryPhoto = false
           where p.user.id = :userId
           """)
    void clearPrimaryPhotos(
            @Param("userId") Long userId
    );
}