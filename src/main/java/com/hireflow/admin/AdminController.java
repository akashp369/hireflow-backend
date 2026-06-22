package com.hireflow.admin;

import com.hireflow.admin.dto.DashboardDto;
import com.hireflow.application.dto.ApplicationDto;
import com.hireflow.common.ApiResponse;
import com.hireflow.common.PageResponse;
import com.hireflow.job.dto.JobDto;
import com.hireflow.user.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<PageResponse<UserProfileDto>>> getAllUsers(Pageable pageable) {
        PageResponse<UserProfileDto> users = adminService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success("Users fetched", users));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted"));
    }

    @GetMapping("/jobs")
    public ResponseEntity<ApiResponse<PageResponse<JobDto>>> getAllJobs(Pageable pageable) {
        PageResponse<JobDto> jobs = adminService.getAllJobs(pageable);
        return ResponseEntity.ok(ApiResponse.success("Jobs fetched", jobs));
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable Long id) {
        adminService.deleteJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job deleted"));
    }

    @GetMapping("/applications")
    public ResponseEntity<ApiResponse<PageResponse<ApplicationDto>>> getAllApplications(Pageable pageable) {
        PageResponse<ApplicationDto> applications = adminService.getAllApplications(pageable);
        return ResponseEntity.ok(ApiResponse.success("Applications fetched", applications));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardDto>> getDashboard() {
        DashboardDto dashboard = adminService.getDashboard();
        return ResponseEntity.ok(ApiResponse.success("Dashboard stats", dashboard));
    }
}
