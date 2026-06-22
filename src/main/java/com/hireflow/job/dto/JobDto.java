package com.hireflow.job.dto;

import com.hireflow.common.enums.ExperienceLevel;
import com.hireflow.common.enums.JobType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {

    private Long id;
    private String title;
    private String description;
    private String requirements;
    private String location;
    private JobType jobType;
    private ExperienceLevel experienceLevel;
    private Double salaryMin;
    private Double salaryMax;
    private Boolean isActive;
    private Long companyId;
    private String companyName;
    private String companyLogoUrl;
    private Long postedById;
    private String postedByName;
    private LocalDateTime createdAt;
    private LocalDate deadline;
}
