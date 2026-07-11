package com.example.serviceimpl;

import com.example.dto.request.UserSubscriptionFilterDTO;
import com.example.dto.response.UserSubscriptionResponseDTO;
import com.example.dto.response.UserSubscriptionStatsDTO;
import com.example.exception.BadRequestException;
import com.example.exception.ResourceNotFoundException;
import com.example.model.UserSubscription;
import com.example.repository.UserSubscriptionRepository;
import com.example.service.AdminAuditLogService;
import com.example.service.AdminUserSubscriptionService;
import com.example.service.CurrentAdminService;
import com.example.specification.UserSubscriptionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminUserSubscriptionServiceImpl
        implements AdminUserSubscriptionService {

    private final UserSubscriptionRepository repository;
    private final CurrentAdminService currentAdminService;

    private final AdminAuditLogService auditLogService;
    @Override
    public Page<UserSubscriptionResponseDTO> getAllSubscriptions(
            UserSubscriptionFilterDTO filter,
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAll(
                UserSubscriptionSpecification.getSubscriptions(filter),
                pageable
        ).map(this::mapToResponse);
    }

    private UserSubscriptionResponseDTO mapToResponse(
            UserSubscription subscription
    ) {

        return UserSubscriptionResponseDTO.builder()
                .id(subscription.getId())

                .userId(
                        subscription.getUser() != null
                                ? subscription.getUser().getId()
                                : null
                )

                .userName(
                        subscription.getUser() != null
                                ? subscription.getUser().getFullName()
                                : null
                )

                .planId(
                        subscription.getSubscriptionPlan() != null
                                ? subscription.getSubscriptionPlan().getId()
                                : null
                )

                .planName(
                        subscription.getSubscriptionPlan() != null
                                ? subscription.getSubscriptionPlan().getName()
                                : null
                )

                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())

                .isActive(subscription.getIsActive())
                .status(subscription.getStatus())

                .createdAt(subscription.getCreatedAt())
                .updatedAt(subscription.getUpdatedAt())

                .build();
    }
    @Override
    public UserSubscriptionResponseDTO getSubscriptionById(Long id) {

        UserSubscription subscription = repository.findWithDetailsById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subscription not found"));
        return UserSubscriptionResponseDTO.builder()
                .id(subscription.getId())
                .userId(subscription.getUser().getId())
                .userName(subscription.getUser().getFullName())
                .planId(subscription.getSubscriptionPlan().getId())
                .planName(subscription.getSubscriptionPlan().getName())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .status(subscription.getStatus())
                .isActive(subscription.getIsActive())
                .createdAt(subscription.getCreatedAt())
                .updatedAt(subscription.getUpdatedAt())
                .build();
    }

    @Override
    @Transactional
    public UserSubscriptionResponseDTO cancelSubscription(Long id, String reason) {

        UserSubscription subscription = repository.findWithDetailsById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subscription not found"));
        String oldStatus = subscription.getStatus();
        Boolean oldActive = subscription.getIsActive();
        if (!Boolean.TRUE.equals(subscription.getIsActive())) {
            throw new BadRequestException("Subscription is already inactive.");
        }

        subscription.setIsActive(false);
        subscription.setStatus("CANCELLED");
        subscription.setCancellationReason(reason);
        subscription.setCancelledAt(java.time.LocalDateTime.now());

        UserSubscription saved = repository.save(subscription);
        auditLogService.log(
                currentAdminService.getCurrentAdmin().getId(),
                "SUBSCRIPTION_MANAGEMENT",
                "SUBSCRIPTION_CANCELLED",
                "USER_SUBSCRIPTION",
                saved.getId(),
                "Cancelled subscription for user: " + saved.getUser().getFullName(),
                "Status=" + oldStatus + ", Active=" + oldActive,
                "Status=CANCELLED, Active=false, Reason=" + reason,
                "SYSTEM",
                "SYSTEM"
        );
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public UserSubscriptionResponseDTO activateSubscription(Long id) {

        UserSubscription subscription = repository.findWithDetailsById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subscription not found"));
        String oldStatus = subscription.getStatus();
        Boolean oldActive = subscription.getIsActive();

        // Deactivate existing active subscription of same user
        repository.findByUserIdAndIsActiveTrue(subscription.getUser().getId())
                .ifPresent(existing -> {

                    if (!existing.getId().equals(subscription.getId())) {

                        String previousStatus = existing.getStatus();
                        Boolean previousActive = existing.getIsActive();

                        existing.setIsActive(false);
                        existing.setStatus("CANCELLED");

                        UserSubscription oldSubscription = repository.save(existing);

                        // Audit previous subscription deactivation
                        auditLogService.log(
                                currentAdminService.getCurrentAdmin().getId(),
                                "SUBSCRIPTION_MANAGEMENT",
                                "PREVIOUS_SUBSCRIPTION_DEACTIVATED",
                                "USER_SUBSCRIPTION",
                                oldSubscription.getId(),
                                "Previous active subscription deactivated for user: "
                                        + oldSubscription.getUser().getFullName(),
                                "Status=" + previousStatus + ", Active=" + previousActive,
                                "Status=CANCELLED, Active=false",
                                "SYSTEM",
                                "SYSTEM"
                        );
                    }
                });

        // Activate selected subscription
        subscription.setIsActive(true);
        subscription.setStatus("ACTIVE");

        // Clear cancellation details
        subscription.setCancellationReason(null);
        subscription.setCancelledAt(null);

        UserSubscription saved = repository.save(subscription);

        // Audit activation
        auditLogService.log(
                currentAdminService.getCurrentAdmin().getId(),
                "SUBSCRIPTION_MANAGEMENT",
                "SUBSCRIPTION_ACTIVATED",
                "USER_SUBSCRIPTION",
                saved.getId(),
                "Activated subscription for user: " + saved.getUser().getFullName(),
                "Status=" + oldStatus + ", Active=" + oldActive,
                "Status=ACTIVE, Active=true",
                "SYSTEM",
                "SYSTEM"
        );

        return mapToResponse(saved);
    }
    @Override
    @Transactional
    public UserSubscriptionResponseDTO expireSubscription(Long id) {

        UserSubscription subscription = repository.findWithDetailsById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subscription not found"));
        if ("EXPIRED".equalsIgnoreCase(subscription.getStatus())) {
            throw new BadRequestException("Subscription is already expired.");
        }

        // Save old values for audit log
        String oldStatus = subscription.getStatus();
        Boolean oldActive = subscription.getIsActive();

        // Expire subscription
        subscription.setIsActive(false);
        subscription.setStatus("EXPIRED");

        UserSubscription saved = repository.save(subscription);

        // Audit Log
        auditLogService.log(
                currentAdminService.getCurrentAdmin().getId(),
                "SUBSCRIPTION_MANAGEMENT",
                "SUBSCRIPTION_EXPIRED",
                "USER_SUBSCRIPTION",
                saved.getId(),
                "Expired subscription for user: " + saved.getUser().getFullName(),
                "Status=" + oldStatus + ", Active=" + oldActive,
                "Status=EXPIRED, Active=false",
                "SYSTEM",
                "SYSTEM"
        );

        return mapToResponse(saved);
    }
    @Override
    @Transactional
    public UserSubscriptionResponseDTO refundSubscription(
            Long id,
            BigDecimal refundAmount,
            String refundReason
    ) {

        UserSubscription subscription = repository.findWithDetailsById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Subscription not found"));
        if ("REFUNDED".equalsIgnoreCase(subscription.getStatus())) {
            throw new BadRequestException("Subscription is already refunded.");
        }

        // Save old values for audit log
        String oldStatus = subscription.getStatus();
        Boolean oldActive = subscription.getIsActive();

        // Refund subscription
        subscription.setIsActive(false);
        subscription.setStatus("REFUNDED");
        subscription.setRefundAmount(refundAmount);
        subscription.setRefundReason(refundReason);
        subscription.setRefundDate(java.time.LocalDateTime.now());

        UserSubscription saved = repository.save(subscription);

        // Audit Log
        auditLogService.log(
                currentAdminService.getCurrentAdmin().getId(),
                "SUBSCRIPTION_MANAGEMENT",
                "SUBSCRIPTION_REFUNDED",
                "USER_SUBSCRIPTION",
                saved.getId(),
                "Refunded subscription for user: " + saved.getUser().getFullName(),
                "Status=" + oldStatus + ", Active=" + oldActive,
                "Status=REFUNDED, Active=false, Refund Amount=" + refundAmount + ", Reason=" + refundReason,
                "SYSTEM",
                "SYSTEM"
        );

        return mapToResponse(saved);
    }
    @Override
    public UserSubscriptionStatsDTO getStatistics() {

        return UserSubscriptionStatsDTO.builder()
                .totalSubscriptions(repository.count())
                .activeSubscriptions(repository.countByIsActiveTrue())
                .inactiveSubscriptions(repository.countByIsActiveFalse())
                .activeStatus(repository.countByStatus("ACTIVE"))
                .cancelledStatus(repository.countByStatus("CANCELLED"))
                .expiredStatus(repository.countByStatus("EXPIRED"))
                .refundedStatus(repository.countByStatus("REFUNDED"))
                .build();
    }

}