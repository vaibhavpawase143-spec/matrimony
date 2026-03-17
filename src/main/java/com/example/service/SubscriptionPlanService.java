package com.example.service;

import com.example.model.SubscriptionPlan;

import java.util.List;
import java.util.Optional;

public interface SubscriptionPlanService {

    SubscriptionPlan create(SubscriptionPlan plan);

    List<SubscriptionPlan> getAll();

    List<SubscriptionPlan> getAllActive();

    List<SubscriptionPlan> getAllInactive();

    Optional<SubscriptionPlan> getById(Long id);

    SubscriptionPlan update(Long id, SubscriptionPlan plan);

    void delete(Long id);

    List<SubscriptionPlan> search(String keyword);

    List<SubscriptionPlan> getByAdmin(Long adminId);

    List<SubscriptionPlan> getActiveByAdmin(Long adminId);
}