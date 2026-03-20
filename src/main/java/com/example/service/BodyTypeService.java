package com.example.service;

import com.example.model.BodyType;
import java.util.List;

public interface BodyTypeService {

    // ✅ Get all body types
    List<BodyType> getAll();

    // ✅ Get active body types
    List<BodyType> getActive();

    // ✅ Get inactive body types
    List<BodyType> getInactive();

    // ✅ Get by ID
    BodyType getById(Long id);

    // ✅ Create body type
    BodyType create(BodyType bodyType);

    // ✅ Update body type
    BodyType update(Long id, BodyType bodyType);

    // ✅ Delete
    void delete(Long id);

    // ✅ Get by admin
    List<BodyType> getByAdmin(Long adminId);

    // ✅ Get active by admin
    List<BodyType> getActiveByAdmin(Long adminId);
}