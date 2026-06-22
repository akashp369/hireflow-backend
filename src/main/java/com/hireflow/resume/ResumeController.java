package com.hireflow.resume;

import com.hireflow.common.ApiResponse;
import com.hireflow.resume.dto.ResumeDto;
import com.hireflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resume")
@PreAuthorize("hasRole('SEEKER')")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ResumeDto>> uploadResume(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file) {
        ResumeDto resume = resumeService.uploadResume(user, file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Resume uploaded", resume));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ResumeDto>>> getMyResumes(@AuthenticationPrincipal User user) {
        List<ResumeDto> resumes = resumeService.getMyResumes(user);
        return ResponseEntity.ok(ApiResponse.success("Resumes fetched", resumes));
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<ApiResponse<ResumeDto>> setDefault(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        ResumeDto resume = resumeService.setDefault(user, id);
        return ResponseEntity.ok(ApiResponse.success("Default resume updated", resume));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        resumeService.deleteResume(user, id);
        return ResponseEntity.ok(ApiResponse.success("Resume deleted"));
    }
}
