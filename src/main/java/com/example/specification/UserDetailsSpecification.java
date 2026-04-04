package com.example.specification;

import com.example.model.UserDetails;
import org.springframework.data.jpa.domain.Specification;

public class UserDetailsSpecification {

    public static Specification<UserDetails> filter(
            String religion,
            String caste,
            String city,
            String education
    ) {
        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            if (religion != null && !religion.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.equal(cb.lower(root.get("religion")), religion.toLowerCase()));
            }

            if (caste != null && !caste.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.equal(cb.lower(root.get("caste")), caste.toLowerCase()));
            }

            if (city != null && !city.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.equal(cb.lower(root.get("city")), city.toLowerCase()));
            }

            if (education != null && !education.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.equal(cb.lower(root.get("education")), education.toLowerCase()));
            }

            return predicate;
        };
    }
}