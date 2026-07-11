package com.example.specification;

import com.example.model.PartnerPreference;
import com.example.model.Profile;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProfileSpecification {

    public static Specification<Profile> matchPreferences(PartnerPreference pref) {

        return (root, query, cb) -> {

            // 🔥 FETCH JOIN (avoid N+1 issue)
            if (query.getResultType() != Long.class) {
                root.fetch("religion", JoinType.LEFT);
                root.fetch("caste", JoinType.LEFT);
                root.fetch("city", JoinType.LEFT);
                root.fetch("educationLevel", JoinType.LEFT);
                root.fetch("height", JoinType.LEFT);
                root.fetch("user", JoinType.LEFT);
                query.distinct(true);
            }

            List<Predicate> predicates = new ArrayList<>();

            // ✅ ACTIVE USERS ONLY (VERY IMPORTANT)
            predicates.add(cb.isTrue(root.get("isActive")));
            predicates.add(cb.isTrue(root.get("user").get("isActive")));
            predicates.add(
                    cb.isTrue(
                            root.get("profileCompleted")
                    )
            );
            // 🎯 RELIGION FILTER
            if (pref.getReligion() != null) {
                predicates.add(cb.equal(
                        root.get("religion").get("id"),
                        pref.getReligion().getId()
                ));
            }

            // 🎯 CASTE FILTER
            if (pref.getCaste() != null) {
                predicates.add(cb.equal(
                        root.get("caste").get("id"),
                        pref.getCaste().getId()
                ));
            }

            // 🎯 CITY FILTER
            // CITY
            if (pref.getCity() != null) {

                predicates.add(
                        cb.equal(
                                root.get("city").get("id"),
                                pref.getCity().getId()
                        )
                );
            }

// EDUCATION
            if (pref.getEducationLevel() != null) {

                predicates.add(
                        cb.equal(
                                root.get("educationLevel").get("id"),
                                pref.getEducationLevel().getId()
                        )
                );
            }

// OCCUPATION
            if (pref.getOccupation() != null) {

                predicates.add(
                        cb.equal(
                                root.get("occupation").get("id"),
                                pref.getOccupation().getId()
                        )
                );
            }

// MARITAL STATUS
            if (pref.getMaritalStatus() != null) {

                predicates.add(
                        cb.equal(
                                root.get("maritalStatus").get("id"),
                                pref.getMaritalStatus().getId()
                        )
                );
            }

            // RELIGION
            if (pref.getReligion() != null) {
                predicates.add(cb.equal(
                        root.get("religion").get("id"),
                        pref.getReligion().getId()
                ));
            }

// CASTE
            if (pref.getCaste() != null) {
                predicates.add(cb.equal(
                        root.get("caste").get("id"),
                        pref.getCaste().getId()
                ));
            }

// CITY
            if (pref.getCity() != null) {
                predicates.add(cb.equal(
                        root.get("city").get("id"),
                        pref.getCity().getId()
                ));
            }

// EDUCATION
            if (pref.getEducationLevel() != null) {
                predicates.add(cb.equal(
                        root.get("educationLevel").get("id"),
                        pref.getEducationLevel().getId()
                ));
            }

// OCCUPATION
            if (pref.getOccupation() != null) {
                predicates.add(cb.equal(
                        root.get("occupation").get("id"),
                        pref.getOccupation().getId()
                ));
            }

// MARITAL STATUS
            if (pref.getMaritalStatus() != null) {
                predicates.add(cb.equal(
                        root.get("maritalStatus").get("id"),
                        pref.getMaritalStatus().getId()
                ));
            }

            // 🎯 AGE FILTER (DOB BASED)
            if (pref.getMinAge() != null && pref.getMaxAge() != null) {

                LocalDate today = LocalDate.now();

                LocalDate maxDob = today.minusYears(pref.getMinAge()); // younger bound
                LocalDate minDob = today.minusYears(pref.getMaxAge()); // older bound

                predicates.add(cb.between(
                        root.get("dateOfBirth"),
                        minDob,
                        maxDob
                ));
            }

            // HEIGHT FILTER

            if (
                    pref.getMinHeight() != null &&
                            pref.getMaxHeight() != null
            ) {

                predicates.add(

                        cb.between(

                                root.get("height").get("id"),

                                pref.getMinHeight(),

                                pref.getMaxHeight()

                        )

                );
            }
            if (
                    pref.getMinWeight() != null &&
                            pref.getMaxWeight() != null
            ) {

                predicates.add(
                        cb.between(
                                root.get("weight").get("id"),
                                pref.getMinWeight(),
                                pref.getMaxWeight()
                        )
                );
            }
            // 🚫 EXCLUDE NULL USERS (SAFETY)
            predicates.add(cb.isNotNull(root.get("user")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}