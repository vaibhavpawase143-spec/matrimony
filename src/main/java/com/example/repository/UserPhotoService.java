package com.example.service;

import com.example.model.UserPhoto;

import java.util.List;

public interface UserPhotoService {

    UserPhoto uploadPhoto(Long userId, String photoType, String photoUrl);

    List<UserPhoto> getUserPhotos(Long userId);

    List<UserPhoto> getByType(Long userId, String photoType);

    void deleteByType(Long userId, String photoType);
}