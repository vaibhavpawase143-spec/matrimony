package com.example.serviceimpl;

import com.example.model.*;
import com.example.repository.*;
import com.example.service.CacheService;
import com.example.service.MatchNotificationService;
import com.example.service.PartnerPreferenceService;
import com.example.service.MatchAsyncService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

@Service
public class PartnerPreferenceServiceImpl implements PartnerPreferenceService {

    private final PartnerPreferenceRepository repository;
    private final UserRepository userRepository;
    private final ReligionRepository religionRepository;
    private final CasteRepository casteRepository;
    private final CityRepository cityRepository;
    private final HeightRepository heightRepository;

    private final MatchAsyncService asyncService;
    private final CacheService cacheService;
    private final MatchNotificationService matchNotificationService;

    public PartnerPreferenceServiceImpl(
            PartnerPreferenceRepository repository,
            UserRepository userRepository,
            ReligionRepository religionRepository,
            CasteRepository casteRepository,
            CityRepository cityRepository,
            HeightRepository heightRepository,
            MatchAsyncService asyncService,
            CacheService cacheService,
            MatchNotificationService matchNotificationService
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.religionRepository = religionRepository;
        this.casteRepository = casteRepository;
        this.cityRepository = cityRepository;
        this.heightRepository = heightRepository;
        this.asyncService = asyncService;
        this.cacheService = cacheService;
        this.matchNotificationService = matchNotificationService;
    }
    private int extractHeightInCm(String height) {
        if (height == null || height.isBlank()) {
            return 0;
        }

        try {
            Matcher matcher = Pattern.compile("\\((\\d+)\\s*cm\\)").matcher(height);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
            Matcher numMatcher = Pattern.compile("(\\d+)").matcher(height);
            if (numMatcher.find()) {
                return Integer.parseInt(numMatcher.group(1));
            }
        } catch (Exception e) {
            // Fallback gracefully
        }
        return 0;
    }

    // ✅ CREATE + UPDATE (UPSERT FIX)

    @CacheEvict(
            value = {
                    "user:partnerPreference",
                    "user:discover",
                    "topMatches"
            },
            allEntries = true
    )
    @Override
    public PartnerPreference savePreference(PartnerPreference preference){

        // 🔥 NULL CHECK
        if (preference.getUser() == null || preference.getUser().getId() == null) {
            throw new RuntimeException("User ID must not be null");
        }

        Long userId = preference.getUser().getId();

        // 🔥 FETCH USER
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ✅ FIX: Perform UPSERT if ID is null but preference already exists for user
        if (preference.getId() == null) {
            Optional<PartnerPreference> existing = repository.findByUserId(userId);
            if (existing.isPresent()) {
                preference.setId(existing.get().getId());
                if (preference.getVersion() == null) {
                    preference.setVersion(existing.get().getVersion());
                }
            }
        }

        preference.setUser(user);

        // 🔥 VALIDATION
        if (preference.getMinAge() != null && preference.getMaxAge() != null &&
                preference.getMinAge() > preference.getMaxAge()) {
            throw new RuntimeException("Min age cannot be greater than max age");
        }

        if (preference.getMinHeight() != null && preference.getMaxHeight() != null) {
            try {
                Height minHeight = heightRepository
                        .findByIdAndDeletedAtIsNull(preference.getMinHeight())
                        .orElse(null);

                Height maxHeight = heightRepository
                        .findByIdAndDeletedAtIsNull(preference.getMaxHeight())
                        .orElse(null);

                if (minHeight != null && maxHeight != null) {
                    int minHeightCm = extractHeightInCm(minHeight.getHeight());
                    int maxHeightCm = extractHeightInCm(maxHeight.getHeight());

                    if (minHeightCm > 0 && maxHeightCm > 0 && minHeightCm > maxHeightCm) {
                        throw new RuntimeException("Min height cannot be greater than max height");
                    }
                }
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception e) {
                // Ignore height parsing issues
            }
        }
        // 🔥 RELATION HANDLING
        if (preference.getReligion() != null && preference.getReligion().getId() != null) {
            preference.setReligion(
                    religionRepository.findById(preference.getReligion().getId())
                            .orElse(null)
            );
        }

        if (preference.getCaste() != null && preference.getCaste().getId() != null) {
            preference.setCaste(
                    casteRepository.findById(preference.getCaste().getId())
                            .orElse(null)
            );
        }

        if (preference.getCity() != null && preference.getCity().getId() != null) {
            preference.setCity(
                    cityRepository.findById(preference.getCity().getId())
                            .orElse(null)
            );
        }
        System.out.println("==================================");
        System.out.println("Preference ID      = " + preference.getId());
        System.out.println("User ID            = " + preference.getUser().getId());
        System.out.println("==================================");
        PartnerPreference saved = repository.save(preference);

        try {
            // Refresh matches asynchronously
            asyncService.preloadMatches(userId);

            // Generate notifications
            matchNotificationService.generateForPreferenceUpdate(userId);
        } catch (Exception e) {
            System.err.println("Async match notification trigger failed: " + e.getMessage());
        }

        return saved;
    }

    // ================= BASIC METHODS =================

    @Override
    public Optional<PartnerPreference> getById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Cacheable(
            value = "user:partnerPreference",
            key = "#userId"
    )
    public Optional<PartnerPreference> getByUserId(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<PartnerPreference> getAll() {
        return repository.findAll();
    }

    @Override
    @CacheEvict(
            value = {
                    "user:partnerPreference",
                    "user:discover",
                    "topMatches"
            },
            allEntries = true
    )
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