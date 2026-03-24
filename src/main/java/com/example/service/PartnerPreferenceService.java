package com.example.service;

import com.example.model.PartnerPreference;

import java.util.List;
import java.util.Optional;

public interface PartnerPreferenceService {

    // ✅ Create / Save preference
    PartnerPreference savePreference(PartnerPreference preference);

    // ✅ Get by ID
    Optional<PartnerPreference> getById(Long id);

    // ✅ Get by userId
    Optional<PartnerPreference> getByUserId(Long userId);

    // ✅ Get all
    List<PartnerPreference> getAll();

    // ✅ Delete
    void delete(Long id);

    // 🔍 Filters
    List<PartnerPreference> getByReligion(Long religionId);

    List<PartnerPreference> getByCaste(Long casteId);

    List<PartnerPreference> getByCity(Long cityId);

    // 🔥 Advanced filters
    List<PartnerPreference> getByReligionAndCaste(Long religionId, Long casteId);

    List<PartnerPreference> getByReligionAndCity(Long religionId, Long cityId);

    List<PartnerPreference> getByCasteAndCity(Long casteId, Long cityId);
}