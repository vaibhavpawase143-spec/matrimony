package com.example.specification;

import com.example.dto.request.UserFilterDTO;
import com.example.model.Profile;
import com.example.model.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecification {

    public static Specification<User> getUsers(UserFilterDTO filter) {

        return (root, query, cb) -> {

            // 🔥 IMPORTANT (avoid duplicate + allow fetch)
            if (query.getResultType() != Long.class) {
                root.fetch("roles", JoinType.LEFT);
                query.distinct(true);
            }

            Predicate predicate = cb.conjunction();
            Join<User, Profile> profileJoin = root.join("profile", JoinType.LEFT);
            // 🔍 SEARCH
            if (filter.getSearch() != null && !filter.getSearch().isBlank()) {

                String like = "%" + filter.getSearch().toLowerCase() + "%";

                predicate = cb.and(predicate,
                        cb.or(
                                cb.like(cb.lower(root.get("firstName")), like),
                                cb.like(cb.lower(root.get("lastName")), like),
                                cb.like(cb.lower(root.get("email")), like)
                        ));
            }

            // ✅ ACTIVE
            if (filter.getIsActive() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("isActive"), filter.getIsActive()));
            }
            if (filter.getIsBlocked() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(root.get("isBlocked"), filter.getIsBlocked())
                );
            }
            if (filter.getEmailVerified() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(root.get("emailVerified"), filter.getEmailVerified())
                );
            }
            if (filter.getPhoneVerified() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(root.get("phoneVerified"), filter.getPhoneVerified())
                );
            }
            // 🗑️ DELETED
            if (filter.getIsDeleted() != null) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("isDeleted"), filter.getIsDeleted()));
            }

            // 🎭 ROLE FILTER
            if (filter.getRole() != null && !filter.getRole().isEmpty()) {

                Join<Object, Object> roleJoin = root.join("roles", JoinType.LEFT);

                predicate = cb.and(predicate,
                        cb.equal(roleJoin.get("name"), filter.getRole()));
            }
            if (filter.getGenderId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoin.get("gender").get("id"),
                                filter.getGenderId()
                        )
                );
            }
            if (filter.getReligionId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoin.get("religion").get("id"),
                                filter.getReligionId()
                        )
                );
            }
            if (filter.getCasteId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoin.get("caste").get("id"),
                                filter.getCasteId()
                        )
                );
            }
            if (filter.getSubCasteId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoin.get("subCaste").get("id"),
                                filter.getSubCasteId()
                        )
                );
            }
            if (filter.getCountryId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoin.get("country").get("id"),
                                filter.getCountryId()
                        )
                );
            }
            if (filter.getStateId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoin.get("state").get("id"),
                                filter.getStateId()
                        )
                );
            }
            if (filter.getCityId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoin.get("city").get("id"),
                                filter.getCityId()
                        )
                );
            }
            if (filter.getMaritalStatusId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoin.get("maritalStatus").get("id"),
                                filter.getMaritalStatusId()
                        )
                );
            }
            if (filter.getEducationLevelId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoin.get("educationLevel").get("id"),
                                filter.getEducationLevelId()
                        )
                );
            }
            if (filter.getOccupationId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoin.get("occupation").get("id"),
                                filter.getOccupationId()
                        )
                );
            }
            if (filter.getIsPremium() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoin.get("isPremium"),
                                filter.getIsPremium()
                        )
                );
            }
            if (filter.getRegisteredFrom() != null) {

                predicate = cb.and(
                        predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getRegisteredFrom().atStartOfDay()
                        )
                );
            }

            if (filter.getRegisteredTo() != null) {

                predicate = cb.and(
                        predicate,
                        cb.lessThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getRegisteredTo().atTime(23,59,59)
                        )
                );
            }
            LocalDate today = LocalDate.now();

            if (filter.getMinAge() != null) {

                LocalDate maxDob = today.minusYears(filter.getMinAge());

                predicate = cb.and(
                        predicate,
                        cb.lessThanOrEqualTo(
                                profileJoin.get("dateOfBirth"),
                                maxDob
                        )
                );
            }

            if (filter.getMaxAge() != null) {

                LocalDate minDob = today.minusYears(filter.getMaxAge() + 1).plusDays(1);

                predicate = cb.and(
                        predicate,
                        cb.greaterThanOrEqualTo(
                                profileJoin.get("dateOfBirth"),
                                minDob
                        )
                );
            }
            return predicate;
        };
    }
}