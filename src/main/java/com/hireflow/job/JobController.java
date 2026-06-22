package com.hireflow.job;

import com.hireflow.common.ApiResponse;
import com.hireflow.common.PageResponse;
import com.hireflow.common.enums.ExperienceLevel;
import com.hireflow.common.enums.JobType;
import com.hireflow.job.dto.CreateJobRequest;
import com.hireflow.job.dto.JobDto;
import com.hireflow.job.dto.UpdateJobRequest;
import com.hireflow.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<JobDto>> createJob(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateJobRequest request) {
        JobDto job = jobService.createJob(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Job posted", job));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<JobDto>>> getAllActiveJobs(Pageable pageable) {
        PageResponse<JobDto> jobs = jobService.getAllActiveJobs(pageable);
        return ResponseEntity.ok(ApiResponse.success("Jobs fetched", jobs));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobDto>> getJobById(@PathVariable Long id) {
        JobDto job = jobService.getJobById(id);
        return ResponseEntity.ok(ApiResponse.success("Job fetched", job));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<JobDto>> updateJob(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody UpdateJobRequest request) {
        JobDto job = jobService.updateJob(user, id, request);
        return ResponseEntity.ok(ApiResponse.success("Job updated", job));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<Void>> deleteJob(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        jobService.deleteJob(user, id);
        return ResponseEntity.ok(ApiResponse.success("Job deleted"));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<PageResponse<JobDto>>> getMyJobs(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        PageResponse<JobDto> jobs = jobService.getMyJobs(user, pageable);
        return ResponseEntity.ok(ApiResponse.success("Jobs fetched", jobs));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<JobDto>>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) JobType jobType,
            @RequestParam(required = false) ExperienceLevel experienceLevel,
            @RequestParam(required = false) Double salaryMin,
            Pageable pageable) {
        PageResponse<JobDto> jobs = jobService.searchJobs(title, location, jobType, experienceLevel, salaryMin, pageable);
        return ResponseEntity.ok(ApiResponse.success("Search results", jobs));
    }
}
