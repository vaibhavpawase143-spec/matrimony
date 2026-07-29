package com.example.specification;

import com.example.model.AdminAuditLog;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class AdminAuditLogSpecification {

    private AdminAuditLogSpecification() {
    }

    public static Specification<AdminAuditLog> hasSearch(String search) {
        return (root, query, cb) -> {

            if (search == null || search.isBlank()) {
                return cb.conjunction();
            }

            String keyword = "%" + search.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.<String>get("description")), keyword),
                    cb.like(cb.lower(root.<String>get("module")), keyword),
                    cb.like(cb.lower(root.<String>get("action")), keyword),
                    cb.like(cb.lower(root.<String>get("entityType")), keyword),
                    cb.like(cb.lower(root.join("admin").<String>get("name")), keyword)
            );
        };
    }

    public static Specification<AdminAuditLog> hasModule(String module) {
        return (root, query, cb) -> {

            if (module == null || module.isBlank()) {
                return cb.conjunction();
            }

            return cb.equal(root.get("module"), module);
        };
    }

    public static Specification<AdminAuditLog> hasAction(String action) {
        return (root, query, cb) -> {

            if (action == null || action.isBlank()) {
                return cb.conjunction();
            }

            return cb.equal(root.get("action"), action);
        };
    }

    public static Specification<AdminAuditLog> hasAdmin(Long adminId) {
        return (root, query, cb) -> {

            if (adminId == null) {
                return cb.conjunction();
            }

            return cb.equal(root.join("admin").get("id"), adminId);
        };
    }

    public static Specification<AdminAuditLog> createdBetween(
            LocalDate fromDate,
            LocalDate toDate
    ) {

        return (root, query, cb) -> {

            if (fromDate == null && toDate == null) {
                return cb.conjunction();
            }

            LocalDateTime from = fromDate != null
                    ? fromDate.atStartOfDay()
                    : LocalDateTime.MIN;

            LocalDateTime to = toDate != null
                    ? toDate.atTime(23, 59, 59)
                    : LocalDateTime.MAX;

            return cb.between(root.get("createdAt"), from, to);
        };
    }
}