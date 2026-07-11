package com.example.specification;

import com.example.dto.request.AdminFilterDTO;
import com.example.model.Admin;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AdminSpecification {

    private AdminSpecification() {
    }

    public static Specification<Admin> filter(AdminFilterDTO filter) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Prevent duplicate results
            query.distinct(true);

            // ================= KEYWORD =================

            if (filter.getKeyword() != null && !filter.getKeyword().trim().isEmpty()) {

                String keyword = "%" + filter.getKeyword().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("name")), keyword),
                                cb.like(cb.lower(root.get("username")), keyword),
                                cb.like(cb.lower(root.get("email")), keyword)
                        )
                );
            }

            // ================= ROLE =================

            if (filter.getRole() != null && !filter.getRole().trim().isEmpty()) {

                predicates.add(
                        cb.equal(
                                root.get("role").get("name"),
                                filter.getRole()
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