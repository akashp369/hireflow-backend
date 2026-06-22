package com.hireflow.application;

import com.hireflow.application.dto.ApplicationDto;
import com.hireflow.application.dto.ApplyJobRequest;
import com.hireflow.application.dto.UpdateStatusRequest;
import com.hireflow.common.ApiResponse;
import com.hireflow.common.PageResponse;
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
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping
    @PreAuthorize("hasRole('SEEKER')")
    public ResponseEntity<ApiResponse<ApplicationDto>> apply(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ApplyJobRequest request) {
        ApplicationDto application = applicationService.apply(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Application submitted", application));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('SEEKER')")
    public ResponseEntity<ApiResponse<PageResponse<ApplicationDto>>> getMyApplications(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        PageResponse<ApplicationDto> applications = applicationService.getMyApplications(user, pageable);
        return ResponseEntity.ok(ApiResponse.success("Applications fetched", applications));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationDto>> getApplicationById(@PathVariable Long id) {
        ApplicationDto application = applicationService.getApplicationById(id);
        return ResponseEntity.ok(ApiResponse.success("Application fetched", application));
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<PageResponse<ApplicationDto>>> getApplicationsForJob(
            @AuthenticationPrincipal User user,
            @PathVariable Long jobId,
            Pageable pageable) {
        PageResponse<ApplicationDto> applications = applicationService.getApplicationsForJob(user, jobId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Applications fetched", applications));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<ApplicationDto>> updateStatus(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {
        ApplicationDto application = applicationService.updateStatus(user, id, request);
        return ResponseEntity.ok(ApiResponse.success("Status updated", application));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SEEKER')")
    public ResponseEntity<ApiResponse<Void>> withdrawApplication(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        applicationService.withdrawApplication(user, id);
        return ResponseEntity.ok(ApiResponse.success("Application withdrawn"));
    }
}
