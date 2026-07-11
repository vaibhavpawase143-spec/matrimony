package com.example.specification;

import com.example.dto.request.PaymentFilterDTO;
import com.example.model.Payment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PaymentSpecification {

    public static Specification<Payment> getPayments(
            PaymentFilterDTO filter
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // ===============================
            // SEARCH
            // ===============================

            if (filter.getSearch() != null &&
                    !filter.getSearch().trim().isEmpty()) {

                String keyword =
                        "%" + filter.getSearch().toLowerCase() + "%";

                predicates.add(

                        cb.or(

                                cb.like(
                                        cb.lower(root.get("transactionId")),
                                        keyword
                                ),

                                cb.like(
                                        cb.lower(root.get("user").get("fullName")),
                                        keyword
                                ),

                                cb.like(
                                        cb.lower(root.get("user").get("email")),
                                        keyword
                                )

                        )

                );
            }

            // ===============================
            // STATUS
            // ===============================

            if (filter.getStatus() != null &&
                    !filter.getStatus().isBlank()) {

                predicates.add(
                        cb.equal(
                                root.get("status"),
                                filter.getStatus()
                        )
                );
            }

            // ===============================
            // PAYMENT METHOD
            // ===============================

            if (filter.getPaymentMethod() != null &&
                    !filter.getPaymentMethod().isBlank()) {

                predicates.add(
                        cb.equal(
                                root.get("paymentMethod"),
                                filter.getPaymentMethod()
                        )
                );
            }

            // ===============================
            // PLAN
            // ===============================

            if (filter.getPlanId() != null) {

                predicates.add(
                        cb.equal(
                                root.get("subscriptionPlan").get("id"),
                                filter.getPlanId()
                        )
                );
            }

            // ===============================
            // MIN AMOUNT
            // ===============================

            if (filter.getMinAmount() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("amount"),
                                filter.getMinAmount()
                        )
                );
            }

            // ===============================
            // MAX AMOUNT
            // ===============================

            if (filter.getMaxAmount() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("amount"),
                                filter.getMaxAmount()
                        )
                );
            }

            // ===============================
            // FROM DATE
            // ===============================

            if (filter.getFromDate() != null) {

                predicates.add(

                        cb.greaterThanOrEqualTo(

                                root.get("createdAt"),

                                filter.getFromDate().atStartOfDay()

                        )

                );

            }

            // ===============================
            // TO DATE
            // ===============================

            if (filter.getToDate() != null) {

                predicates.add(

                        cb.lessThanOrEqualTo(

                                root.get("createdAt"),

                                filter.getToDate().atTime(23, 59, 59)

                        )

                );

            }

            return cb.and(
                    predicates.toArray(new Predicate[0])
            );

        };

    }

}