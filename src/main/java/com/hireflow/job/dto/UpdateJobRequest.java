package com.hireflow.job.dto;

import com.hireflow.common.enums.ExperienceLevel;
import com.hireflow.common.enums.JobType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateJobRequest {

    private String title;
    private String description;
    private String requirements;
    private String location;
    private JobType jobType;
    private ExperienceLevel experienceLevel;
    private Double salaryMin;
    private Double salaryMax;
    private Boolean isActive;
    private LocalDate deadline;
}
