package com.hireflow.savedjob;

import com.hireflow.common.PageResponse;
import com.hireflow.common.exception.BadRequestException;
import com.hireflow.common.exception.ResourceNotFoundException;
import com.hireflow.job.Job;
import com.hireflow.job.JobRepository;
import com.hireflow.job.dto.JobDto;
import com.hireflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;

    public void saveJob(User user, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (savedJobRepository.existsByUserIdAndJobId(user.getId(), jobId)) {
            throw new BadRequestException("Job already saved");
        }

        SavedJob savedJob = SavedJob.builder()
                .user(user)
                .job(job)
                .build();

        savedJobRepository.save(savedJob);
    }

    public void unsaveJob(User user, Long jobId) {
        SavedJob savedJob = savedJobRepository.findByUserIdAndJobId(user.getId(), jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Saved job not found"));
        savedJobRepository.delete(savedJob);
    }

    public PageResponse<JobDto> getSavedJobs(User user, Pageable pageable) {
        Page<SavedJob> page = savedJobRepository.findByUserId(user.getId(), pageable);
        return PageResponse.<JobDto>builder()
                .content(page.getContent().stream().map(this::mapToJobDto).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private JobDto mapToJobDto(SavedJob savedJob) {
        Job job = savedJob.getJob();
        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .location(job.getLocation())
                .jobType(job.getJobType())
                .experienceLevel(job.getExperienceLevel())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .isActive(job.getIsActive())
                .companyId(job.getCompany().getId())
                .companyName(job.getCompany().getName())
                .companyLogoUrl(job.getCompany().getLogoUrl())
                .postedById(job.getPostedBy().getId())
                .postedByName(job.getPostedBy().getName())
                .createdAt(job.getCreatedAt())
                .deadline(job.getDeadline())
                .build();
    }
}
