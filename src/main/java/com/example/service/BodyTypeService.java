package com.example.service;

import com.example.model.BodyType;
import java.util.List;

public interface BodyTypeService {

    BodyType create(BodyType bodyType, Long adminId);

    BodyType getById(Long id, Long adminId);

    List<BodyType> getAll(Long adminId);

    List<BodyType> getActive(Long adminId);

    List<BodyType> getInactive(Long adminId);

    BodyType update(Long id, BodyType bodyType, Long adminId);

    void delete(Long id, Long adminId);
}