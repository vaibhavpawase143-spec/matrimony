package com.example.repository;

import com.example.model.UserPhoto;
import com.example.model.PhotoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

    List<UserPhoto> findByUserId(Long userId);

    List<UserPhoto> findByUserIdAndPhotoType(Long userId, PhotoType photoType);

    Optional<UserPhoto> findFirstByUserIdAndPhotoType(Long userId, PhotoType photoType);

    boolean existsByUserIdAndPhotoType(Long userId, PhotoType photoType);

    void deleteByUserIdAndPhotoType(Long userId, PhotoType photoType);
}