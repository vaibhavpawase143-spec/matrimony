package com.example.specification;

import com.example.dto.request.UserSubscriptionFilterDTO;
import com.example.model.UserSubscription;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UserSubscriptionSpecification {

    public static Specification<UserSubscription> getSubscriptions(
            UserSubscriptionFilterDTO filter
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            // ================= SEARCH =================

            if (filter.getSearch() != null &&
                    !filter.getSearch().trim().isEmpty()) {

                String search = "%" + filter.getSearch().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(
                                        cb.lower(root.get("user").get("firstName")),
                                        search
                                ),
                                cb.like(
                                        cb.lower(root.get("user").get("lastName")),
                                        search
                                ),
                                cb.like(
                                        cb.lower(
                                                cb.concat(
                                                        cb.concat(
                                                                root.get("user").get("firstName"),
                                                                " "
                                                        ),
                                                        root.get("user").get("lastName")
                                                )
                                        ),
                                        search
                                ),
                                cb.like(
                                        cb.lower(root.get("user").get("email")),
                                        search
                                ),
                                cb.like(
                                        cb.lower(root.get("subscriptionPlan").get("name")),
                                        search
                                )
                        )
                );
            }

            // ================= STATUS =================

            if (filter.getStatus() != null &&
                    !filter.getStatus().trim().isEmpty()) {

                predicates.add(
                        cb.equal(
                                root.get("status"),
                                filter.getStatus()
                        )
                );
            }

            // ================= ACTIVE =================

            if (filter.getIsActive() != null) {

                predicates.add(
                        cb.equal(
                                root.get("isActive"),
                                filter.getIsActive()
                        )
                );
            }

            // ================= PLAN =================

            if (filter.getPlanId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("subscriptionPlan").get("id"),
                                filter.getPlanId()
                        )
                );
            }

            // ================= FROM DATE =================

            if (filter.getFromDate() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getFromDate().atStartOfDay()
                        )
                );
            }

            // ================= TO DATE =================

            if (filter.getToDate() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("createdAt"),
                                filter.getToDate().atTime(23, 59, 59)
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}