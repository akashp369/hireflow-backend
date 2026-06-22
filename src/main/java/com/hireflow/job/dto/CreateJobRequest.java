package com.hireflow.job.dto;

import com.hireflow.common.enums.ExperienceLevel;
import com.hireflow.common.enums.JobType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateJobRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
    private String requirements;
    private String location;

    @NotNull(message = "Job type is required")
    private JobType jobType;

    private ExperienceLevel experienceLevel;
    private Double salaryMin;
    private Double salaryMax;
    private LocalDate deadline;
}
