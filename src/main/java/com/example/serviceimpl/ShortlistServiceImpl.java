package com.example.serviceimpl;

import com.example.model.NotificationType;
import com.example.model.Shortlist;
import com.example.repository.ShortlistRepository;
import com.example.service.NotificationService;
import com.example.service.ShortlistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        if(
                shortlist.getProfile()
                        .getUser()
                        .getId()
                        .equals(userId)
        ){

            throw new RuntimeException(
                    "You cannot shortlist yourself"
            );

        }
        System.out.println(
                "USER = " + userId +
                        " PROFILE = " + profileId
        );

        System.out.println(
                "EXISTS = " +

                        repository
                                .findByUser_IdAndProfile_Id(
                                        userId,
                                        profileId
                                )

                                .isPresent()

        );
        Optional<Shortlist> existing =
                repository.findByUser_IdAndProfile_Id(userId, profileId);

        // 🔥 If already exists
        if (existing.isPresent()) {

            Shortlist old = existing.get();

            if (Boolean.TRUE.equals(old.getIsActive())) {

                throw new RuntimeException(
                        "Profile already shortlisted!"
                );

            }

            old.setIsActive(true);

            Shortlist updated =
                    repository.save(old);

            Long receiverId =
                    old.getProfile()
                            .getUser()
                            .getId();

            notificationService.create(
                    userId,
                    receiverId,
                    NotificationType.SHORTLIST
            );

            return updated;
        }

        // ✅ New shortlist
        Shortlist saved = repository.save(shortlist);

        // 🔔 Notification
        Long receiverId =
                shortlist
                        .getProfile()
                        .getUser()
                        .getId();

        notificationService.create(
                userId,
                receiverId,
                NotificationType.SHORTLIST
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

    // 🔍 Get paginated shortlisted profiles by user
    @Override
    public Page<Shortlist> getByUser(Long userId, Pageable pageable) {
        return repository.findByUser_IdAndIsActiveTrue(userId, pageable);
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