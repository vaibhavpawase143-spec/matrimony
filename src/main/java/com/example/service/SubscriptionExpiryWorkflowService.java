package com.example.service;

import com.example.model.SubscriptionExpiryJob;

import java.util.List;

public interface SubscriptionExpiryWorkflowService {

    List<SubscriptionExpiryJob> processExpiringSubscriptions();

    void processAndPublishExpiringSubscriptions();

    void publishPendingOutboxJobs();
}
