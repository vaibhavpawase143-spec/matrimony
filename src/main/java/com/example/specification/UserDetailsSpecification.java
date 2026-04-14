package com.example.specification;

import com.example.dto.request.UserDetailsSearchRequest;
import com.example.model.UserDetails;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class UserDetailsSpecification {

    public static Specification<UserDetails> filter(UserDetailsSearchRequest req) {

        return (root, query, cb) -> {

            Predicate p = cb.conjunction();

            // ===== BASIC FILTER =====
            if (req.getReligion() != null && !req.getReligion().isEmpty()) {
                p = cb.and(p,
                        cb.equal(cb.lower(root.get("religion")), req.getReligion().toLowerCase()));
            }

            if (req.getCaste() != null && !req.getCaste().isEmpty()) {
                p = cb.and(p,
                        cb.equal(cb.lower(root.get("caste")), req.getCaste().toLowerCase()));
            }

            if (req.getCity() != null && !req.getCity().isEmpty()) {
                p = cb.and(p,
                        cb.equal(cb.lower(root.get("city")), req.getCity().toLowerCase()));
            }

            if (req.getEducation() != null && !req.getEducation().isEmpty()) {
                p = cb.and(p,
                        cb.equal(cb.lower(root.get("education")), req.getEducation().toLowerCase()));
            }

            // ===== AGE FILTER =====
            if (req.getMinAge() != null) {
                p = cb.and(p,
                        cb.greaterThanOrEqualTo(root.get("minAge"), req.getMinAge()));
            }

            if (req.getMaxAge() != null) {
                p = cb.and(p,
                        cb.lessThanOrEqualTo(root.get("maxAge"), req.getMaxAge()));
            }

            // ===== HEIGHT FILTER =====
            if (req.getMinHeight() != null) {
                p = cb.and(p,
                        cb.greaterThanOrEqualTo(root.get("minHeight"), req.getMinHeight()));
            }

            if (req.getMaxHeight() != null) {
                p = cb.and(p,
                        cb.lessThanOrEqualTo(root.get("maxHeight"), req.getMaxHeight()));
            }

            // ===== PREMIUM FILTER =====
            if (Boolean.TRUE.equals(req.getOnlyPremium())) {
                p = cb.and(p,
                        cb.equal(root.get("subscriptionStatus"), "ACTIVE"));
            }

            // ===== ACTIVE USERS =====
            if (Boolean.TRUE.equals(req.getOnlyActive())) {
                p = cb.and(p,
                        cb.equal(root.get("isActive"), true));
            }

            // ===== KEYWORD SEARCH 🔥 =====
            if (req.getKeyword() != null && !req.getKeyword().isEmpty()) {

                String like = "%" + req.getKeyword().toLowerCase() + "%";

                p = cb.and(p, cb.or(
                        cb.like(cb.lower(root.get("fullName")), like),
                        cb.like(cb.lower(root.get("city")), like),
                        cb.like(cb.lower(root.get("occupation")), like)
                ));
            }

            return p;
        };
    }
}