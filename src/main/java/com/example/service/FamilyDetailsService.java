package com.example.service;

import com.example.dto.request.FamilyDetailsRequestDto;
import com.example.dto.response.FamilyDetailsResponseDto;

import java.util.List;

public interface FamilyDetailsService {

    // =========================
    // CRUD
    // =========================

    FamilyDetailsResponseDto create(FamilyDetailsRequestDto requestDto);

    FamilyDetailsResponseDto update(Long id, FamilyDetailsRequestDto requestDto);

    void softDelete(Long id);

    void restore(Long id);

    void hardDelete(Long id);

    // =========================
    // GET
    // =========================

    FamilyDetailsResponseDto getById(Long id);

    List<FamilyDetailsResponseDto> getAll();

    List<FamilyDetailsResponseDto> getDeleted();

    // =========================
    // PROFILE
    // =========================

    FamilyDetailsResponseDto getByProfile(Long profileId);

    boolean existsByProfile(Long profileId);

    List<FamilyDetailsResponseDto> getActiveByProfile(Long profileId);

    // =========================
    // FAMILY TYPE
    // =========================

    List<FamilyDetailsResponseDto> getByFamilyType(Long familyTypeId);

    // =========================
    // FAMILY
    // =========================

    List<FamilyDetailsResponseDto> getByFamily(Long familyId);

    List<FamilyDetailsResponseDto> getActiveByFamily(Long familyId);

    // =========================
    // BROTHER TYPE
    // =========================

    List<FamilyDetailsResponseDto> getByBrotherType(Long brotherTypeId);

    // =========================
    // SISTER TYPE
    // =========================

    List<FamilyDetailsResponseDto> getBySisterType(Long sisterTypeId);
}