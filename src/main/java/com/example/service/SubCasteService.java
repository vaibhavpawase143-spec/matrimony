package com.example.service;

import com.example.model.SubCaste;

import java.util.List;
import java.util.Optional;

public interface SubCasteService {

    SubCaste create(SubCaste subCaste);

    List<SubCaste> getAll();

    List<SubCaste> getAllActive();

    Optional<SubCaste> getById(Long id);

    SubCaste update(Long id, SubCaste subCaste);

    void delete(Long id);

    List<SubCaste> search(String keyword);

    // ✅ Get active SubCastes by Admin
    List<SubCaste> getActiveByAdmin(Long adminId);

    List<SubCaste> getByCaste(Long casteId);

    List<SubCaste> getActiveByCaste(Long casteId);

    List<SubCaste> getByAdmin(Long adminId);
}