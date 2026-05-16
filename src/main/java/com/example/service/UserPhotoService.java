package com.example.service;

import com.example.model.PhotoType;
import com.example.model.UserPhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserPhotoService {

    String upload(MultipartFile file, PhotoType type);

    List<String> uploadMultiple(List<MultipartFile> files);

    void delete(PhotoType type);

    List<UserPhoto> getMyPhotos();

    String getMyProfilePhoto();
}