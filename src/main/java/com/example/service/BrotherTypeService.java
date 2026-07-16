package com.example.service;

import com.example.model.BrotherType;

import java.util.List;

public interface BrotherTypeService {

    BrotherType create(BrotherType brotherType, Long adminId);

    BrotherType getById(Long id, Long adminId);

    List<BrotherType> getAll(Long adminId);

    List<BrotherType> getActive(Long adminId);

    BrotherType update(Long id, BrotherType brotherType, Long adminId);

    void delete(Long id, Long deletedBy);

    void hardDelete(Long id);

    BrotherType restore(Long id);

    List<BrotherType> getDeleted(Long adminId);
}