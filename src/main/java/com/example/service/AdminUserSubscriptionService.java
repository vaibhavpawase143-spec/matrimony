package com.example.service;

import com.example.dto.request.UserSubscriptionFilterDTO;
import com.example.dto.response.UserSubscriptionResponseDTO;
import com.example.dto.response.UserSubscriptionStatsDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface AdminUserSubscriptionService {

    // Get All User Subscriptions
    Page<UserSubscriptionResponseDTO> getAllSubscriptions(
            UserSubscriptionFilterDTO filter,
            int page,
            int size,
            String sortBy,
            String direction
    );
    UserSubscriptionResponseDTO getSubscriptionById(Long id);
    UserSubscriptionResponseDTO cancelSubscription(
            Long id,
            String reason
    );

    UserSubscriptionResponseDTO activateSubscription(
            Long id
    );

    UserSubscriptionResponseDTO expireSubscription(
            Long id
    );

    UserSubscriptionResponseDTO refundSubscription(
            Long id,
            BigDecimal refundAmount,
            String refundReason
    );

    UserSubscriptionStatsDTO getStatistics();

}