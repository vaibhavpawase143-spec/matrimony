package com.example.specification;

import com.example.dto.request.SubscriptionPlanFilterDTO;
import com.example.model.SubscriptionPlan;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionPlanSpecification {

    public static Specification<SubscriptionPlan> getPlans(
            SubscriptionPlanFilterDTO filter
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
                                cb.like(cb.lower(root.get("name")), search),
                                cb.like(cb.lower(root.get("description")), search)
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

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}