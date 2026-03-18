package com.example.service;

import com.example.model.BrotherType;
import java.util.List;

public interface BrotherTypeService {

    // ✅ Get all
    List<BrotherType> getAll();

    // ✅ Get active
    List<BrotherType> getActive();

    // ✅ Get by ID
    BrotherType getById(Long id);

    // ✅ Create
    BrotherType create(BrotherType bt);

    // ✅ Update
    BrotherType update(Long id, BrotherType bt);

    // ✅ Delete
    void delete(Long id);

    // ✅ Get by admin
    List<BrotherType> getByAdmin(Long adminId);

    // ✅ Get active by admin
    List<BrotherType> getActiveByAdmin(Long adminId);
}