package com.doctor_service.query.service;

import com.doctor_service.command.dtos.DoctorStatus;
import com.doctor_service.command.entities.Doctors;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class DoctorSpecification {
    
    private static final Logger logger = LoggerFactory.getLogger(DoctorSpecification.class);

    public static Specification<Doctors> buildSpecification(DoctorSearchCriteria criteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Create a list for OR conditions
            List<Predicate> orPredicates = new ArrayList<>();

            // Keyword search
            if (StringUtils.hasText(criteria.getKeyword())) {
                String keyword = "%" + criteria.getKeyword().toLowerCase() + "%";
                logger.info("Keyword applied in search: {}", keyword);

                orPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), keyword));
                orPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), keyword));
                orPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), keyword));
                orPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), keyword));
                orPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("registrationNumber")), keyword));
                orPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("specialization")), keyword));
                orPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("clinicName")), keyword));
                orPredicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("clinicLocation")), keyword));
            }

            // Specialization filter
            if (StringUtils.hasText(criteria.getSpecialization())) {
                orPredicates.add(criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("specialization")), 
                    criteria.getSpecialization().toLowerCase()
                ));
            }

            // Location filter
            if (StringUtils.hasText(criteria.getLocation())) {
                orPredicates.add(criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("clinicLocation")), 
                    criteria.getLocation().toLowerCase()
                ));
            }

            // Clinic name filter
            if (StringUtils.hasText(criteria.getClinicName())) {
                orPredicates.add(criteriaBuilder.equal(
                    criteriaBuilder.lower(root.get("clinicName")), 
                    criteria.getClinicName().toLowerCase()
                ));
            }

            // Experience filter
            if (criteria.getMinExperience() != null) {
                orPredicates.add(criteriaBuilder.greaterThanOrEqualTo(
                    root.get("yearsOfExperience"), 
                    criteria.getMinExperience()
                ));
            }

            // Consultation fee filter
            if (criteria.getMaxFee() != null) {
                orPredicates.add(criteriaBuilder.lessThanOrEqualTo(
                    root.get("consultationFee"), 
                    criteria.getMaxFee()
                ));
            }

            // Add the OR conditions if any exist
            if (!orPredicates.isEmpty()) {
                predicates.add(criteriaBuilder.or(orPredicates.toArray(new Predicate[0])));
            }

            // Status filter - Keep this as an AND condition since it's a critical business rule
            if (criteria.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), criteria.getStatus()));
            } else {
                // By default, only show active doctors if status is not specified
                predicates.add(criteriaBuilder.equal(root.get("status"), DoctorStatus.VERIFIED));
            }

            // Combine the OR predicates with the status predicate using AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}