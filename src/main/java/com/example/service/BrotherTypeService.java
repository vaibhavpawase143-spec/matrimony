package com.example.service;

import com.example.model.BrotherType;
import java.util.List;

public interface BrotherTypeService {

    BrotherType create(BrotherType entity, Long adminId);

    BrotherType getById(Long id, Long adminId);

    List<BrotherType> getAll(Long adminId);

    List<BrotherType> getActive(Long adminId);

    BrotherType update(Long id, BrotherType entity, Long adminId);

    void delete(Long id, Long adminId);

    List<BrotherType> search(String keyword, Long adminId);
}