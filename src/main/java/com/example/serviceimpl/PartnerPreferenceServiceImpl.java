package com.example.serviceimpl;

import com.example.model.*;
import com.example.repository.*;
import com.example.service.CacheService;
import com.example.service.PartnerPreferenceService;
import com.example.service.MatchAsyncService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerPreferenceServiceImpl implements PartnerPreferenceService {

    private final PartnerPreferenceRepository repository;
    private final UserRepository userRepository;
    private final ReligionRepository religionRepository;
    private final CasteRepository casteRepository;
    private final CityRepository cityRepository;

    private final MatchAsyncService asyncService;
    private final CacheService cacheService;

    public PartnerPreferenceServiceImpl(
            PartnerPreferenceRepository repository,
            UserRepository userRepository,
            ReligionRepository religionRepository,
            CasteRepository casteRepository,
            CityRepository cityRepository,
            MatchAsyncService asyncService,
            CacheService cacheService
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.religionRepository = religionRepository;
        this.casteRepository = casteRepository;
        this.cityRepository = cityRepository;
        this.asyncService = asyncService;
        this.cacheService = cacheService;
    }

    // ✅ CREATE + UPDATE (FINAL FIX)
    @Override
    public PartnerPreference savePreference(PartnerPreference preference) {

        // 🔥 NULL CHECK
        if (preference.getUser() == null || preference.getUser().getId() == null) {
            throw new RuntimeException("User ID must not be null");
        }

        Long userId = preference.getUser().getId();

        // 🔥 FETCH USER
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ FIX: Only block duplicate on CREATE
        if (preference.getId() == null && repository.existsByUserId(userId)) {
            throw new RuntimeException("Preference already exists for this user!");
        }

        preference.setUser(user);

        // 🔥 VALIDATION
        if (preference.getMinAge() != null && preference.getMaxAge() != null &&
                preference.getMinAge() > preference.getMaxAge()) {
            throw new RuntimeException("Min age cannot be greater than max age");
        }

        if (preference.getMinHeight() != null && preference.getMaxHeight() != null &&
                preference.getMinHeight() > preference.getMaxHeight()) {
            throw new RuntimeException("Min height cannot be greater than max height");
        }

        // 🔥 RELATION HANDLING
        if (preference.getReligion() != null && preference.getReligion().getId() != null) {
            preference.setReligion(
                    religionRepository.findById(preference.getReligion().getId())
                            .orElseThrow(() -> new RuntimeException("Religion not found"))
            );
        }

        if (preference.getCaste() != null && preference.getCaste().getId() != null) {
            preference.setCaste(
                    casteRepository.findById(preference.getCaste().getId())
                            .orElseThrow(() -> new RuntimeException("Caste not found"))
            );
        }

        if (preference.getCity() != null && preference.getCity().getId() != null) {
            preference.setCity(
                    cityRepository.findById(preference.getCity().getId())
                            .orElseThrow(() -> new RuntimeException("City not found"))
            );
        }

        PartnerPreference saved = repository.save(preference);

        // 🔥 ASYNC MATCH PRELOAD
        asyncService.preloadMatches(userId);

        return saved;
    }

    // ================= BASIC METHODS =================

    @Override
    public Optional<PartnerPreference> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<PartnerPreference> getByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<PartnerPreference> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // ================= FILTER METHODS =================

    @Override
    public List<PartnerPreference> getByReligion(Long religionId) {
        return repository.findByReligionId(religionId);
    }

    @Override
    public List<PartnerPreference> getByCaste(Long casteId) {
        return repository.findByCasteId(casteId);
    }

    @Override
    public List<PartnerPreference> getByCity(Long cityId) {
        return repository.findByCityId(cityId);
    }

    // ✅ FIXED (was empty before)
    @Override
    public List<PartnerPreference> getByReligionAndCaste(Long religionId, Long casteId) {
        return repository.findByReligionIdAndCasteId(religionId, casteId);
    }

    @Override
    public List<PartnerPreference> getByReligionAndCity(Long religionId, Long cityId) {
        return repository.findByReligionIdAndCityId(religionId, cityId);
    }

    @Override
    public List<PartnerPreference> getByCasteAndCity(Long casteId, Long cityId) {
        return repository.findByCasteIdAndCityId(casteId, cityId);
    }
}