package com.example.specification;

import com.example.dto.request.ReportFilterDTO;
import com.example.model.UserReport;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ReportSpecification {

    public static Specification<UserReport> getReports(
            ReportFilterDTO filter
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return cb.and(predicates.toArray(new Predicate[0]));
            }

            // ================= SEARCH =================

            if (filter.getSearch() != null &&
                    !filter.getSearch().trim().isEmpty()) {

                String search =
                        "%" + filter.getSearch().toLowerCase() + "%";

                predicates.add(

                        cb.or(

                                cb.like(
                                        cb.lower(root.get("reason")),
                                        search
                                ),

                                cb.like(
                                        cb.lower(root.get("reporter").get("fullName")),
                                        search
                                ),

                                cb.like(
                                        cb.lower(root.get("reportedUser").get("fullName")),
                                        search
                                )
                        )
                );
            }

            // ================= STATUS =================

            if (filter.getStatus() != null) {

                predicates.add(
                        cb.equal(
                                root.get("status"),
                                filter.getStatus()
                        )
                );
            }

            // ================= REPORTER =================

            if (filter.getReporterName() != null &&
                    !filter.getReporterName().trim().isEmpty()) {

                predicates.add(

                        cb.like(

                                cb.lower(
                                        root.get("reporter").get("fullName")
                                ),

                                "%" +
                                        filter.getReporterName().toLowerCase()
                                        + "%"
                        )
                );
            }

            // ================= REPORTED USER =================

            if (filter.getReportedUserName() != null &&
                    !filter.getReportedUserName().trim().isEmpty()) {

                predicates.add(

                        cb.like(

                                cb.lower(
                                        root.get("reportedUser").get("fullName")
                                ),

                                "%" +
                                        filter.getReportedUserName().toLowerCase()
                                        + "%"
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
                                filter.getToDate().atTime(23,59,59)
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}