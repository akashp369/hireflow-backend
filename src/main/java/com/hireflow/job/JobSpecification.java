package com.hireflow.job;

import com.hireflow.common.enums.ExperienceLevel;
import com.hireflow.common.enums.JobType;
import org.springframework.data.jpa.domain.Specification;

public class JobSpecification {

    public static Specification<Job> isActive() {
        return (root, query, cb) -> cb.isTrue(root.get("isActive"));
    }

    public static Specification<Job> hasTitle(String title) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Job> hasLocation(String location) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%");
    }

    public static Specification<Job> hasJobType(JobType jobType) {
        return (root, query, cb) -> cb.equal(root.get("jobType"), jobType);
    }

    public static Specification<Job> hasExperienceLevel(ExperienceLevel level) {
        return (root, query, cb) -> cb.equal(root.get("experienceLevel"), level);
    }

    public static Specification<Job> hasSalaryMin(Double salaryMin) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("salaryMin"), salaryMin);
    }
}
