package com.example.service;

import com.example.model.DisabilityStatus;

import java.util.List;

public interface DisabilityStatusService {

    // ==========================
    // CRUD
    // ==========================

    DisabilityStatus create(DisabilityStatus disabilityStatus);

    DisabilityStatus update(Long id, DisabilityStatus disabilityStatus);

    void delete(Long id, Long deletedBy);

    void hardDelete(Long id);

    DisabilityStatus restore(Long id);

    DisabilityStatus getById(Long id);

    List<DisabilityStatus> getAll();

    List<DisabilityStatus> getDeleted();

    // ==========================
    // ACTIVE / INACTIVE
    // ==========================

    List<DisabilityStatus> getActive();

    List<DisabilityStatus> getInactive();

    // ==========================
    // ADMIN
    // ==========================

    List<DisabilityStatus> getByAdmin(Long adminId);

    List<DisabilityStatus> getActiveByAdmin(Long adminId);

    // ==========================
    // SEARCH
    // ==========================

    List<DisabilityStatus> search(String keyword);

    List<DisabilityStatus> searchByAdmin(Long adminId, String keyword);
}