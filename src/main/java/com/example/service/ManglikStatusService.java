package com.example.service;

import com.example.model.ManglikStatus;

import java.util.List;
import java.util.Optional;

public interface ManglikStatusService {

    ManglikStatus save(ManglikStatus manglikStatus);

    Optional<ManglikStatus> getById(Long id);

    Optional<ManglikStatus> getByName(String name);

    List<ManglikStatus> getAll();

    List<ManglikStatus> getAllActive();

    List<ManglikStatus> getAllInactive();

    List<ManglikStatus> getByAdmin(Long adminId);

    List<ManglikStatus> getActiveByAdmin(Long adminId);

    List<ManglikStatus> searchByName(String keyword);

    ManglikStatus update(Long id, ManglikStatus updated);

    void delete(Long id); // soft delete
}