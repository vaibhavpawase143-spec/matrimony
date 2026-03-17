package com.example.service;

import com.example.model.SisterType;

import java.util.List;
import java.util.Optional;

public interface SisterTypeService {

    SisterType create(SisterType sisterType);

    List<SisterType> getAll();

    List<SisterType> getAllActive();

    List<SisterType> getAllInactive();

    Optional<SisterType> getById(Long id);

    SisterType update(Long id, SisterType sisterType);

    void delete(Long id);

    List<SisterType> search(String keyword);

    List<SisterType> getByAdmin(Long adminId);

    List<SisterType> getActiveByAdmin(Long adminId);
}