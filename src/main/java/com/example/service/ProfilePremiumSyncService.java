package com.example.service;

import com.example.model.User;
import com.example.model.UserSubscription;

public interface ProfilePremiumSyncService {

    void sync(User user, UserSubscription subscription);

}