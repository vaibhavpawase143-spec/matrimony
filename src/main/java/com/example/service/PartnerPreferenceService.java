package com.example.service;

import com.example.model.PartnerPreference;

import java.util.List;
import java.util.Optional;

public interface PartnerPreferenceService {

    PartnerPreference create(PartnerPreference preference);

    PartnerPreference update(Long userId, PartnerPreference preference);

    Optional<PartnerPreference> getByUserId(Long userId);

    void delete(Long userId);

    List<PartnerPreference> getByReligion(Long religionId);

    List<PartnerPreference> getByCaste(Long casteId);

    List<PartnerPreference> getByCity(Long cityId);
}