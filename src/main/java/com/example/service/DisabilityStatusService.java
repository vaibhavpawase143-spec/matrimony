package com.example.service;

import com.example.model.DisabilityStatus;
import java.util.List;

public interface DisabilityStatusService {

    List<DisabilityStatus> getAll();

    List<DisabilityStatus> getActive();

    DisabilityStatus getById(Long id);

    DisabilityStatus create(DisabilityStatus ds);

    DisabilityStatus update(Long id, DisabilityStatus updated);

    void delete(Long id);

    List<DisabilityStatus> getByAdmin(Long adminId);

    List<DisabilityStatus> getActiveByAdmin(Long adminId);

    List<DisabilityStatus> search(String keyword);
}