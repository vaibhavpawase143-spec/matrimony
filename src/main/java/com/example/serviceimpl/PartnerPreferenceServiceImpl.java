package com.example.serviceimpl;

import com.example.model.PartnerPreference;
import com.example.repository.PartnerPreferenceRepository;
import com.example.service.CacheService;
import com.example.service.PartnerPreferenceService;
import com.example.service.MatchAsyncService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartnerPreferenceServiceImpl implements PartnerPreferenceService {

    private final PartnerPreferenceRepository repository;
    private final MatchAsyncService asyncService;
    private final CacheService cacheService;

    public PartnerPreferenceServiceImpl(
            PartnerPreferenceRepository repository,
            MatchAsyncService asyncService, CacheService cacheService
    ) {
        this.repository = repository;
        this.asyncService = asyncService;
        this.cacheService = cacheService;
    }

    // ✅ Save preference (with cache eviction + async preload)

    @Override
    public PartnerPreference savePreference(PartnerPreference preference) {

        Long userId = preference.getUser().getId();

        if (repository.existsByUserId(userId)) {
            throw new RuntimeException("Preference already exists for this user!");
        }

        PartnerPreference saved = repository.save(preference);

        // 🔥 CLEAR ONLY THIS USER CACHE
        cacheService.evictUserMatches(userId);

        // 🔥 ASYNC PRELOAD
        asyncService.preloadMatches(userId);

        return saved;
    }
    // ================= NORMAL METHODS =================

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