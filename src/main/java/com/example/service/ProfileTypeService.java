package com.example.service;

import com.example.model.ProfileType;

import java.util.List;
import java.util.Optional;

public interface ProfileTypeService {

    ProfileType create(ProfileType profileType);

    List<ProfileType> getAll();

    Optional<ProfileType> getById(Long id);

    ProfileType update(Long id, ProfileType profileType);

    void delete(Long id);

    List<ProfileType> getByAdmin(Long adminId);

    List<ProfileType> search(String keyword);
}