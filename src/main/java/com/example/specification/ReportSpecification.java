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
                                        cb.lower(root.get("reporter").get("firstName")),
                                        search
                                ),
                                cb.like(
                                        cb.lower(root.get("reporter").get("lastName")),
                                        search
                                ),
                                cb.like(
                                        cb.lower(cb.concat(cb.concat(root.get("reporter").get("firstName"), " "), root.get("reporter").get("lastName"))),
                                        search
                                ),
                                cb.like(
                                        cb.lower(root.get("reportedUser").get("firstName")),
                                        search
                                ),
                                cb.like(
                                        cb.lower(root.get("reportedUser").get("lastName")),
                                        search
                                ),
                                cb.like(
                                        cb.lower(cb.concat(cb.concat(root.get("reportedUser").get("firstName"), " "), root.get("reportedUser").get("lastName"))),
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

                String reporterSearch = "%" + filter.getReporterName().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(
                                        cb.lower(root.get("reporter").get("firstName")),
                                        reporterSearch
                                ),
                                cb.like(
                                        cb.lower(root.get("reporter").get("lastName")),
                                        reporterSearch
                                ),
                                cb.like(
                                        cb.lower(cb.concat(cb.concat(root.get("reporter").get("firstName"), " "), root.get("reporter").get("lastName"))),
                                        reporterSearch
                                )
                        )
                );
            }

            // ================= REPORTED USER =================

            if (filter.getReportedUserName() != null &&
                    !filter.getReportedUserName().trim().isEmpty()) {

                String reportedUserSearch = "%" + filter.getReportedUserName().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(
                                        cb.lower(root.get("reportedUser").get("firstName")),
                                        reportedUserSearch
                                ),
                                cb.like(
                                        cb.lower(root.get("reportedUser").get("lastName")),
                                        reportedUserSearch
                                ),
                                cb.like(
                                        cb.lower(cb.concat(cb.concat(root.get("reportedUser").get("firstName"), " "), root.get("reportedUser").get("lastName"))),
                                        reportedUserSearch
                                )
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