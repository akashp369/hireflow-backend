package com.hireflow.savedjob;

import com.hireflow.common.ApiResponse;
import com.hireflow.common.PageResponse;
import com.hireflow.job.dto.JobDto;
import com.hireflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/saved-jobs")
@PreAuthorize("hasRole('SEEKER')")
@RequiredArgsConstructor
public class SavedJobController {

    private final SavedJobService savedJobService;

    @PostMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Void>> saveJob(
            @AuthenticationPrincipal User user,
            @PathVariable Long jobId) {
        savedJobService.saveJob(user, jobId);
        return ResponseEntity.ok(ApiResponse.success("Job saved"));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Void>> unsaveJob(
            @AuthenticationPrincipal User user,
            @PathVariable Long jobId) {
        savedJobService.unsaveJob(user, jobId);
        return ResponseEntity.ok(ApiResponse.success("Job unsaved"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<JobDto>>> getSavedJobs(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        PageResponse<JobDto> jobs = savedJobService.getSavedJobs(user, pageable);
        return ResponseEntity.ok(ApiResponse.success("Saved jobs fetched", jobs));
    }
}
