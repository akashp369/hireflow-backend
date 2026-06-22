package com.hireflow.application.dto;

import com.hireflow.common.enums.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationDto {

    private Long id;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private Long seekerId;
    private String seekerName;
    private String seekerEmail;
    private Long resumeId;
    private String resumeUrl;
    private String coverLetter;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime updatedAt;
}
