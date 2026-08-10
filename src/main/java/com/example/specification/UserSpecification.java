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

            Predicate predicate = cb.conjunction();
            
            // Helper to lazily obtain profile join or fetch without duplicating joins
            java.util.function.Supplier<Join<User, Profile>> profileJoinSupplier = new java.util.function.Supplier<>() {
                private Join<User, Profile> profileJoin = null;

                @Override
                public Join<User, Profile> get() {
                    if (profileJoin != null) return profileJoin;

                    if (query.getResultType() != Long.class) {
                        profileJoin = (Join<User, Profile>) (Object) root.fetch("profile", JoinType.LEFT);
                    } else {
                        profileJoin = root.join("profile", JoinType.LEFT);
                    }
                    return profileJoin;
                }
            };

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

            // ✅ ACTIVE / INACTIVE FILTER
            if (filter.getIsActive() != null) {
                if (Boolean.TRUE.equals(filter.getIsActive())) {
                    predicate = cb.and(predicate, cb.equal(root.get("isActive"), true));
                } else {
                    predicate = cb.and(predicate, cb.or(
                            cb.equal(root.get("isActive"), false),
                            cb.isNull(root.get("isActive"))
                    ));
                }
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

            // 🎭 ROLE FILTER (Only apply DISTINCT when joining multi-valued roles)
            if (filter.getRole() != null && !filter.getRole().isEmpty()) {
                if (query.getResultType() != Long.class) {
                    query.distinct(true);
                }
                Join<Object, Object> roleJoin = root.join("roles", JoinType.LEFT);

                predicate = cb.and(predicate,
                        cb.equal(roleJoin.get("name"), filter.getRole()));
            }
            if (filter.getGenderId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoinSupplier.get().get("gender").get("id"),
                                filter.getGenderId()
                        )
                );
            }
            if (filter.getReligionId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoinSupplier.get().get("religion").get("id"),
                                filter.getReligionId()
                        )
                );
            }
            if (filter.getCasteId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoinSupplier.get().get("caste").get("id"),
                                filter.getCasteId()
                        )
                );
            }
            if (filter.getSubCasteId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoinSupplier.get().get("subCaste").get("id"),
                                filter.getSubCasteId()
                        )
                );
            }
            if (filter.getCountryId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoinSupplier.get().get("country").get("id"),
                                filter.getCountryId()
                        )
                );
            }
            if (filter.getStateId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoinSupplier.get().get("state").get("id"),
                                filter.getStateId()
                        )
                );
            }
            if (filter.getCityId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoinSupplier.get().get("city").get("id"),
                                filter.getCityId()
                        )
                );
            }
            if (filter.getMaritalStatusId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoinSupplier.get().get("maritalStatus").get("id"),
                                filter.getMaritalStatusId()
                        )
                );
            }
            if (filter.getEducationLevelId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoinSupplier.get().get("educationLevel").get("id"),
                                filter.getEducationLevelId()
                        )
                );
            }
            if (filter.getOccupationId() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoinSupplier.get().get("occupation").get("id"),
                                filter.getOccupationId()
                        )
                );
            }
            if (filter.getIsPremium() != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                profileJoinSupplier.get().get("isPremium"),
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
                                profileJoinSupplier.get().get("dateOfBirth"),
                                maxDob
                        )
                );
            }

            if (filter.getMaxAge() != null) {

                LocalDate minDob = today.minusYears(filter.getMaxAge() + 1).plusDays(1);

                predicate = cb.and(
                        predicate,
                        cb.greaterThanOrEqualTo(
                                profileJoinSupplier.get().get("dateOfBirth"),
                                minDob
                        )
                );
            }

            // Ensure profile is eager fetched for DTO construction if not already fetched
            if (query.getResultType() != Long.class) {
                profileJoinSupplier.get();
            }

            return predicate;
        };
    }
}