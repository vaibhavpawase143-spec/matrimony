package com.example.specification;

import com.example.dto.request.UserFilterDTO;
import com.example.model.User;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> getUsers(UserFilterDTO filter) {

        return (root, query, cb) -> {

            // 🔥 IMPORTANT (avoid duplicate + allow fetch)
            if (query.getResultType() != Long.class) {
                root.fetch("roles", JoinType.LEFT);
                query.distinct(true);
            }

            Predicate predicate = cb.conjunction();

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

            return predicate;
        };
    }
}