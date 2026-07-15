package com.example.serviceimpl;

import com.example.model.Shortlist;
import com.example.model.NotificationType;
import com.example.repository.ShortlistRepository;
import com.example.service.ShortlistService;
import com.example.service.NotificationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShortlistServiceImpl implements ShortlistService {

    private final ShortlistRepository repository;
    private final NotificationService notificationService;

    public ShortlistServiceImpl(ShortlistRepository repository,
                                NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }

    // ✅ Add to shortlist (handles duplicate + reactivation)
    @Override
    public Shortlist addToShortlist(Shortlist shortlist) {

        Long userId = shortlist.getUser().getId();
        Long profileId = shortlist.getProfile().getId();

        Optional<Shortlist> existing =
                repository.findByUser_IdAndProfile_Id(userId, profileId);

        // 🔥 If already exists
        if (existing.isPresent()) {
            Shortlist old = existing.get();

            // 👉 If already active → prevent duplicate
            if (Boolean.TRUE.equals(old.getIsActive())) {
                throw new RuntimeException("Profile already shortlisted!");
            }

            // 👉 If inactive → reactivate
            old.setIsActive(true);
            Shortlist updated = repository.save(old);

            return updated;
        }

        // ✅ New shortlist
        Shortlist saved = repository.save(shortlist);

        // 🔔 Notification
        notificationService.create(
                userId,
                profileId,
                NotificationType.REQUEST // later you can change to SHORTLIST
        );

        return saved;
    }

    // 🔍 Get by ID
    @Override
    public Optional<Shortlist> getById(Long id) {
        return repository.findById(id)
                .filter(s -> Boolean.TRUE.equals(s.getIsActive()));
    }

    // 🔍 Get specific shortlist
    @Override
    public Optional<Shortlist> getByUserAndProfile(Long userId, Long profileId) {
        return repository
                .findByUser_IdAndProfile_Id(userId, profileId)
                .filter(s -> Boolean.TRUE.equals(s.getIsActive()));
    }

    // 🔍 Get all shortlisted profiles by user
    @Override
    public List<Shortlist> getByUser(Long userId) {
        return repository.findByUser_IdAndIsActiveTrue(userId);
    }

    // 🔥 Who shortlisted a profile
    @Override
    public List<Shortlist> getByProfile(Long profileId) {
        return repository.findByProfile_IdAndIsActiveTrue(profileId);
    }

    // ❌ Remove from shortlist (SOFT DELETE)
    @Override
    public void removeFromShortlist(Long userId, Long profileId) {

        Shortlist shortlist = repository
                .findByUser_IdAndProfile_Id(userId, profileId)
                .orElseThrow(() -> new RuntimeException("Shortlist not found"));

        shortlist.setIsActive(false);
        repository.save(shortlist);
    }

    // 🔍 Get all active
    @Override
    public List<Shortlist> getAll() {
        return repository.findByIsActiveTrue();
    }
}