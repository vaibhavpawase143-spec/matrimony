package com.example.service;

import com.example.model.MotherTongue;

import java.util.List;
import java.util.Optional;

public interface MotherTongueService {

    MotherTongue create(MotherTongue motherTongue);

    List<MotherTongue> getAll();

    List<MotherTongue> getAllActive();

    List<MotherTongue> getAllInactive();

    Optional<MotherTongue> getById(Long id);

    MotherTongue update(Long id, MotherTongue motherTongue);

    void delete(Long id);

    List<MotherTongue> search(String keyword);

    List<MotherTongue> getByAdmin(Long adminId);

    List<MotherTongue> getActiveByAdmin(Long adminId);
}