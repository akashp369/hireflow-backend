package com.hireflow.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    Page<JobApplication> findBySeekerId(Long seekerId, Pageable pageable);
    Page<JobApplication> findByJobId(Long jobId, Pageable pageable);
    boolean existsByJobIdAndSeekerId(Long jobId, Long seekerId);
    long countByJobId(Long jobId);
}
