package com.hireflow.job;

import com.hireflow.common.PageResponse;
import com.hireflow.common.enums.ExperienceLevel;
import com.hireflow.common.enums.JobType;
import com.hireflow.common.exception.BadRequestException;
import com.hireflow.common.exception.ResourceNotFoundException;
import com.hireflow.company.Company;
import com.hireflow.company.CompanyRepository;
import com.hireflow.job.dto.CreateJobRequest;
import com.hireflow.job.dto.JobDto;
import com.hireflow.job.dto.UpdateJobRequest;
import com.hireflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;

    public JobDto createJob(User recruiter, CreateJobRequest request) {
        Company company = companyRepository.findByRecruiterId(recruiter.getId())
                .orElseThrow(() -> new BadRequestException("Register a company before posting jobs"));

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .requirements(request.getRequirements())
                .location(request.getLocation())
                .jobType(request.getJobType())
                .experienceLevel(request.getExperienceLevel())
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .deadline(request.getDeadline())
                .company(company)
                .postedBy(recruiter)
                .build();

        jobRepository.save(job);
        return mapToDto(job);
    }

    public PageResponse<JobDto> getAllActiveJobs(Pageable pageable) {
        Page<Job> page = jobRepository.findByIsActiveTrue(pageable);
        return toPageResponse(page);
    }

    public JobDto getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));
        return mapToDto(job);
    }

    public JobDto updateJob(User recruiter, Long id, UpdateJobRequest request) {
        Job job = getOwnedJob(recruiter, id);

        if (request.getTitle() != null) job.setTitle(request.getTitle());
        if (request.getDescription() != null) job.setDescription(request.getDescription());
        if (request.getRequirements() != null) job.setRequirements(request.getRequirements());
        if (request.getLocation() != null) job.setLocation(request.getLocation());
        if (request.getJobType() != null) job.setJobType(request.getJobType());
        if (request.getExperienceLevel() != null) job.setExperienceLevel(request.getExperienceLevel());
        if (request.getSalaryMin() != null) job.setSalaryMin(request.getSalaryMin());
        if (request.getSalaryMax() != null) job.setSalaryMax(request.getSalaryMax());
        if (request.getIsActive() != null) job.setIsActive(request.getIsActive());
        if (request.getDeadline() != null) job.setDeadline(request.getDeadline());

        jobRepository.save(job);
        return mapToDto(job);
    }

    public void deleteJob(User recruiter, Long id) {
        Job job = getOwnedJob(recruiter, id);
        jobRepository.delete(job);
    }

    public PageResponse<JobDto> getMyJobs(User recruiter, Pageable pageable) {
        Page<Job> page = jobRepository.findByPostedById(recruiter.getId(), pageable);
        return toPageResponse(page);
    }

    public PageResponse<JobDto> searchJobs(String title, String location, JobType jobType,
                                           ExperienceLevel experienceLevel, Double salaryMin,
                                           Pageable pageable) {
        Specification<Job> spec = Specification.where(JobSpecification.isActive());

        if (title != null) spec = spec.and(JobSpecification.hasTitle(title));
        if (location != null) spec = spec.and(JobSpecification.hasLocation(location));
        if (jobType != null) spec = spec.and(JobSpecification.hasJobType(jobType));
        if (experienceLevel != null) spec = spec.and(JobSpecification.hasExperienceLevel(experienceLevel));
        if (salaryMin != null) spec = spec.and(JobSpecification.hasSalaryMin(salaryMin));

        Page<Job> page = jobRepository.findAll(spec, pageable);
        return toPageResponse(page);
    }

    private Job getOwnedJob(User recruiter, Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));
        if (!job.getPostedBy().getId().equals(recruiter.getId())) {
            throw new BadRequestException("You can only modify your own jobs");
        }
        return job;
    }

    private PageResponse<JobDto> toPageResponse(Page<Job> page) {
        return PageResponse.<JobDto>builder()
                .content(page.getContent().stream().map(this::mapToDto).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private JobDto mapToDto(Job job) {
        return JobDto.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .requirements(job.getRequirements())
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
