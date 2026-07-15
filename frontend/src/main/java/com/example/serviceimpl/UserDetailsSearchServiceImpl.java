package com.example.serviceimpl;

import com.example.dto.request.UserDetailsSearchRequest;
import com.example.dto.response.UserDetailsResponse;
import com.example.model.UserDetails;
import com.example.repository.UserDetailsRepository;
import com.example.service.CompatibilityService;
import com.example.service.UserDetailsSearchService;
import com.example.specification.UserDetailsSpecification;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsSearchServiceImpl implements UserDetailsSearchService {

    private final UserDetailsRepository repository;
    private final CompatibilityService compatibilityService;

    @Override
    public Page<UserDetailsResponse> search(UserDetailsSearchRequest request) {

        // ✅ Safe pagination (avoid null)
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 10;

        Pageable pageable = PageRequest.of(page, size);

        // ✅ Fetch filtered data
        Page<UserDetails> users = repository.findAll(
                UserDetailsSpecification.filter(request),
                pageable
        );

        // ✅ If no data → return empty directly
        if (users.isEmpty()) {
            return Page.empty(pageable);
        }

        // 🔥 MATCH SORT (IMPORTANT FIX)
        if ("match".equalsIgnoreCase(request.getSortBy())) {

            // ❌ OLD: hardcoded 1L
            // ✅ NEW: use request userId
            if (request.getUserId() == null) {
                throw new RuntimeException("userId is required for match sorting");
            }

            UserDetails currentUser = repository.findByUserId(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<UserDetailsResponse> sorted = users.getContent().stream()
                    .sorted((u1, u2) -> Double.compare(
                            compatibilityService.calculateScore(currentUser, u2),
                            compatibilityService.calculateScore(currentUser, u1)
                    ))
                    .map(this::mapToResponse)
                    .toList();

            return new PageImpl<>(sorted, pageable, users.getTotalElements());
        }

        // ✅ Default mapping
        return users.map(this::mapToResponse);
    }

    // ===== DTO MAPPING =====
    private UserDetailsResponse mapToResponse(UserDetails u) {

        return new UserDetailsResponse(
                u.getUserId(),
                u.getFullName(),
                u.getEmail(),
                u.getPhone(),
                u.getIsActive(),
                u.getRoles(),
                u.getReligion(),
                u.getCaste(),
                u.getEducation(),
                u.getOccupation(),
                u.getHeight(),
                u.getWeight(),
                u.getCity(),
                u.getState(),
                u.getCountry(),
                u.getAbout(),
                u.getFamilyType(),
                u.getFamily(),
                u.getBrotherType(),
                u.getSisterType(),
                u.getFatherOccupation(),
                u.getMotherOccupation(),
                u.getMinAge(),
                u.getMaxAge(),
                u.getMinHeight(),
                u.getMaxHeight(),
                u.getPreferredReligion(),
                u.getPreferredCaste(),
                u.getPreferredCity(),
                u.getPlanName(),
                u.getSubscriptionStatus(),
                null,
                null,
                u.getPaymentStatus(),
                null,
                null,
                u.getCreatedAt()
        );
    }
}