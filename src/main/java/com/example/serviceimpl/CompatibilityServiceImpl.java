package com.example.serviceimpl;

import com.example.model.UserDetails;
import com.example.service.CompatibilityService;
import org.springframework.stereotype.Service;

@Service
public class CompatibilityServiceImpl implements CompatibilityService {

    @Override
    public double calculateScore(UserDetails u1, UserDetails u2) {

        double score = 0;

        // 🔥 RELIGION MATCH (30)
        if (u1.getPreferredReligion() != null &&
                u1.getPreferredReligion().equalsIgnoreCase(u2.getReligion())) {
            score += 30;
        }

        // 🔥 CASTE MATCH (20)
        if (u1.getPreferredCaste() != null &&
                u1.getPreferredCaste().equalsIgnoreCase(u2.getCaste())) {
            score += 20;
        }

        // 🔥 CITY MATCH (15)
        if (u1.getPreferredCity() != null &&
                u1.getPreferredCity().equalsIgnoreCase(u2.getCity())) {
            score += 15;
        }

        // 🔥 AGE MATCH (20)
        if (u1.getMinAge() != null && u1.getMaxAge() != null &&
                u2.getMinAge() != null && u2.getMaxAge() != null) {

            if (u2.getMinAge() >= u1.getMinAge() &&
                    u2.getMaxAge() <= u1.getMaxAge()) {
                score += 20;
            }
        }

        // 🔥 PREMIUM BOOST (15)
        if ("ACTIVE".equalsIgnoreCase(u2.getSubscriptionStatus())) {
            score += 15;
        }

        return score; // max = 100
    }
}