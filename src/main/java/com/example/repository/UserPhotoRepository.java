package com.example.repository;

import com.example.model.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

    // 🔍 Get all photos of user
    List<UserPhoto> findByUserId(Long userId);

    // 🔍 Get by photo type (PROFILE, KUNDALI)
    List<UserPhoto> findByUserIdAndPhotoType(Long userId, String photoType);

    // 🔍 Delete by user + type (useful for replacing profile pic)
    void deleteByUserIdAndPhotoType(Long userId, String photoType);
}