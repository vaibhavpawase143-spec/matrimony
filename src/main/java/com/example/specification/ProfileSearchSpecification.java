package com.example.specification;

import com.example.dto.request.ProfileSearchRequestDTO;
import com.example.model.*;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProfileSearchSpecification {

    public static Specification<Profile> buildSearchSpecification(ProfileSearchRequestDTO req, Long currentUserId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. Mandatory Eligibility Filters
            predicates.add(cb.equal(root.get("isActive"), true));
            predicates.add(cb.equal(root.get("profileCompleted"), true));

            // Define reusable joins for predicates
            Join<Profile, User> userJoin = root.join("user", JoinType.INNER);
            Join<Profile, City> cityJoin = root.join("city", JoinType.LEFT);
            Join<Profile, State> stateJoin = root.join("state", JoinType.LEFT);
            Join<Profile, Occupation> occupationJoin = root.join("occupation", JoinType.LEFT);
            Join<Profile, EducationLevel> educationJoin = root.join("educationLevel", JoinType.LEFT);
            Join<Profile, Religion> religionJoin = root.join("religion", JoinType.LEFT);
            Join<Profile, Caste> casteJoin = root.join("caste", JoinType.LEFT);

            // Fetch associations for result query to eliminate N+1 queries
            boolean isCountQuery = Long.class.equals(query.getResultType()) || long.class.equals(query.getResultType());
            if (!isCountQuery) {
                root.fetch("user", JoinType.INNER);
                root.fetch("city", JoinType.LEFT);
                root.fetch("state", JoinType.LEFT);
                root.fetch("occupation", JoinType.LEFT);
                root.fetch("educationLevel", JoinType.LEFT);
                root.fetch("employed", JoinType.LEFT);
                root.fetch("religion", JoinType.LEFT);
                root.fetch("caste", JoinType.LEFT);
                root.fetch("gender", JoinType.LEFT);
                root.fetch("subCaste", JoinType.LEFT);
                root.fetch("motherTongue", JoinType.LEFT);
                root.fetch("country", JoinType.LEFT);
                root.fetch("income", JoinType.LEFT);
                root.fetch("height", JoinType.LEFT);
                root.fetch("weight", JoinType.LEFT);
                root.fetch("diet", JoinType.LEFT);
                root.fetch("smoking", JoinType.LEFT);
                root.fetch("drinking", JoinType.LEFT);
                root.fetch("manglikStatus", JoinType.LEFT);
                root.fetch("profileType", JoinType.LEFT);
                root.fetch("maritalStatus", JoinType.LEFT);
            }

            predicates.add(cb.equal(userJoin.get("isActive"), true));
            predicates.add(cb.equal(userJoin.get("isBlocked"), false));
            predicates.add(cb.equal(userJoin.get("isDeleted"), false));

            // 2. Hide Logged-in User and Enforce Mutual Blocking Rules
            if (currentUserId != null) {
                predicates.add(cb.notEqual(userJoin.get("id"), currentUserId));

                // Subquery 1: Profiles blocked BY current user
                Subquery<Long> blockedByCurrentSubquery = query.subquery(Long.class);
                Root<UserBlock> ubRoot1 = blockedByCurrentSubquery.from(UserBlock.class);
                blockedByCurrentSubquery.select(ubRoot1.get("blockedId"))
                        .where(
                                cb.equal(ubRoot1.get("blockerId"), currentUserId),
                                cb.equal(ubRoot1.get("isActive"), true)
                        );
                predicates.add(cb.not(userJoin.get("id").in(blockedByCurrentSubquery)));

                // Subquery 2: Profiles where current user IS BLOCKED BY target user
                Subquery<Long> currentBlockedByTargetSubquery = query.subquery(Long.class);
                Root<UserBlock> ubRoot2 = currentBlockedByTargetSubquery.from(UserBlock.class);
                currentBlockedByTargetSubquery.select(ubRoot2.get("blockerId"))
                        .where(
                                cb.equal(ubRoot2.get("blockedId"), currentUserId),
                                cb.equal(ubRoot2.get("isActive"), true)
                        );
                predicates.add(cb.not(userJoin.get("id").in(currentBlockedByTargetSubquery)));
            }

            // 3. Keyword Search (Case-Insensitive across name, email, bio, address, city, state, occupation, education, religion, caste)
            if (req.getSearch() != null && !req.getSearch().trim().isEmpty()) {
                String searchPattern = "%" + req.getSearch().trim().toLowerCase() + "%";

                Predicate firstNameMatch = cb.like(cb.lower(userJoin.get("firstName")), searchPattern);
                Predicate lastNameMatch = cb.like(cb.lower(userJoin.get("lastName")), searchPattern);
                Predicate fullNameMatch = cb.like(cb.lower(userJoin.get("fullName")), searchPattern);
                Predicate emailMatch = cb.like(cb.lower(userJoin.get("email")), searchPattern);
                
                Predicate bioMatch = cb.like(cb.lower(root.get("about")), searchPattern);
                Predicate aboutMeMatch = cb.like(cb.lower(root.get("aboutMe")), searchPattern);
                Predicate addressMatch = cb.like(cb.lower(root.get("address")), searchPattern);
                
                Predicate cityMatch = cb.like(cb.lower(cityJoin.get("name")), searchPattern);
                Predicate stateMatch = cb.like(cb.lower(stateJoin.get("name")), searchPattern);
                Predicate occupationMatch = cb.like(cb.lower(occupationJoin.get("name")), searchPattern);
                Predicate educationMatch = cb.like(cb.lower(educationJoin.get("name")), searchPattern);
                Predicate religionMatch = cb.like(cb.lower(religionJoin.get("name")), searchPattern);
                Predicate casteMatch = cb.like(cb.lower(casteJoin.get("name")), searchPattern);

                predicates.add(cb.or(
                        firstNameMatch, lastNameMatch, fullNameMatch, emailMatch,
                        bioMatch, aboutMeMatch, addressMatch,
                        cityMatch, stateMatch, occupationMatch, educationMatch, religionMatch, casteMatch
                ));
            }

            // 4. Robust Min & Max Age Filters (Date of Birth Range)
            LocalDate now = LocalDate.now();
            Integer ageFrom = req.getAgeFrom();
            Integer ageTo = req.getAgeTo();

            if (ageFrom != null && ageTo != null && ageFrom > ageTo) {
                int temp = ageFrom;
                ageFrom = ageTo;
                ageTo = temp;
            }

            if (ageFrom != null && ageFrom > 0) {
                LocalDate maxDob = now.minusYears(ageFrom);
                predicates.add(cb.lessThanOrEqualTo(root.get("dateOfBirth"), maxDob));
            }
            if (ageTo != null && ageTo > 0) {
                LocalDate minDob = now.minusYears(ageTo + 1).plusDays(1);
                predicates.add(cb.greaterThanOrEqualTo(root.get("dateOfBirth"), minDob));
            }

            // 5. Gender Filter
            if (req.getGenderId() != null) {
                predicates.add(cb.equal(root.get("gender").get("id"), req.getGenderId()));
            } else if (req.getGender() != null && !req.getGender().trim().isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("gender").get("name")), req.getGender().trim().toLowerCase()));
            }

            // 6. Religion, Caste, SubCaste
            if (req.getReligionId() != null) {
                predicates.add(cb.equal(religionJoin.get("id"), req.getReligionId()));
            }
            if (req.getCasteId() != null) {
                predicates.add(cb.equal(casteJoin.get("id"), req.getCasteId()));
            }
            if (req.getSubCasteId() != null) {
                predicates.add(cb.equal(root.get("subCaste").get("id"), req.getSubCasteId()));
            }

            // 7. Mother Tongue & Marital Status
            if (req.getMotherTongueId() != null) {
                predicates.add(cb.equal(root.get("motherTongue").get("id"), req.getMotherTongueId()));
            }
            if (req.getMaritalStatusId() != null) {
                predicates.add(cb.equal(root.get("maritalStatus").get("id"), req.getMaritalStatusId()));
            }

            // 8. Education Level, Occupation, Income
            Long eduId = req.getEducationLevelId() != null ? req.getEducationLevelId() : req.getEducationId();
            if (eduId != null) {
                predicates.add(cb.equal(educationJoin.get("id"), eduId));
            }
            if (req.getOccupationId() != null) {
                predicates.add(cb.equal(occupationJoin.get("id"), req.getOccupationId()));
            }
            if (req.getIncomeId() != null) {
                predicates.add(cb.equal(root.get("income").get("id"), req.getIncomeId()));
            }

            // 9. Height & Weight
            if (req.getHeightId() != null) {
                predicates.add(cb.equal(root.get("height").get("id"), req.getHeightId()));
            }
            if (req.getWeightId() != null) {
                predicates.add(cb.equal(root.get("weight").get("id"), req.getWeightId()));
            }

            // 10. Location (City, State, Country)
            if (req.getCityId() != null) {
                predicates.add(cb.equal(cityJoin.get("id"), req.getCityId()));
            }
            if (req.getStateId() != null) {
                predicates.add(cb.equal(stateJoin.get("id"), req.getStateId()));
            }
            if (req.getCountryId() != null) {
                predicates.add(cb.equal(root.get("country").get("id"), req.getCountryId()));
            }

            // 11. Lifestyle (Diet, Smoking, Drinking)
            if (req.getDietId() != null) {
                predicates.add(cb.equal(root.get("diet").get("id"), req.getDietId()));
            }
            if (req.getSmokingId() != null) {
                predicates.add(cb.equal(root.get("smoking").get("id"), req.getSmokingId()));
            }
            if (req.getDrinkingId() != null) {
                predicates.add(cb.equal(root.get("drinking").get("id"), req.getDrinkingId()));
            }
            // 12. Education Level, Occupation, Employment Status, Income
            Long educationId = req.getEducationLevelId() != null
                    ? req.getEducationLevelId()
                    : req.getEducationId();

            if (eduId != null) {
                predicates.add(
                        cb.equal(
                                educationJoin.get("id"),
                                eduId
                        )
                );
            }

            if (req.getOccupationId() != null) {
                predicates.add(
                        cb.equal(
                                occupationJoin.get("id"),
                                req.getOccupationId()
                        )
                );
            }

// NEW: EMPLOYMENT STATUS FILTER
            if (req.getEmploymentStatusId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("employed").get("id"),
                                req.getEmploymentStatusId()
                        )
                );
            }

            if (req.getIncomeId() != null) {
                predicates.add(
                        cb.equal(
                                root.get("income").get("id"),
                                req.getIncomeId()
                        )
                );
            }

            // 13. Manglik Status & Profile Type
            if (req.getManglikStatusId() != null) {
                predicates.add(cb.equal(root.get("manglikStatus").get("id"), req.getManglikStatusId()));
            }
            if (req.getProfileTypeId() != null) {
                predicates.add(cb.equal(root.get("profileType").get("id"), req.getProfileTypeId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
