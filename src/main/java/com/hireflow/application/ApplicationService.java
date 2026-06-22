package com.hireflow.application;

import com.hireflow.application.dto.ApplicationDto;
import com.hireflow.application.dto.ApplyJobRequest;
import com.hireflow.application.dto.UpdateStatusRequest;
import com.hireflow.common.PageResponse;
import com.hireflow.common.exception.BadRequestException;
import com.hireflow.common.exception.ResourceNotFoundException;
import com.hireflow.job.Job;
import com.hireflow.job.JobRepository;
import com.hireflow.resume.Resume;
import com.hireflow.resume.ResumeRepository;
import com.hireflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final ResumeRepository resumeRepository;

    public ApplicationDto apply(User seeker, ApplyJobRequest request) {
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!job.getIsActive()) {
            throw new BadRequestException("This job is no longer active");
        }

        if (applicationRepository.existsByJobIdAndSeekerId(job.getId(), seeker.getId())) {
            throw new BadRequestException("You have already applied to this job");
        }

        Resume resume = resumeRepository.findByIdAndUserId(request.getResumeId(), seeker.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        JobApplication application = JobApplication.builder()
                .job(job)
                .seeker(seeker)
                .resume(resume)
                .coverLetter(request.getCoverLetter())
                .build();

        applicationRepository.save(application);
        return mapToDto(application);
    }

    public PageResponse<ApplicationDto> getMyApplications(User seeker, Pageable pageable) {
        Page<JobApplication> page = applicationRepository.findBySeekerId(seeker.getId(), pageable);
        return toPageResponse(page);
    }

    public ApplicationDto getApplicationById(Long id) {
        JobApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        return mapToDto(application);
    }

    public PageResponse<ApplicationDto> getApplicationsForJob(User recruiter, Long jobId, Pageable pageable) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (!job.getPostedBy().getId().equals(recruiter.getId())) {
            throw new BadRequestException("You can only view applications for your own jobs");
        }

        Page<JobApplication> page = applicationRepository.findByJobId(jobId, pageable);
        return toPageResponse(page);
    }

    public ApplicationDto updateStatus(User recruiter, Long applicationId, UpdateStatusRequest request) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getJob().getPostedBy().getId().equals(recruiter.getId())) {
            throw new BadRequestException("You can only update status of applications for your own jobs");
        }

        application.setStatus(request.getStatus());
        applicationRepository.save(application);
        return mapToDto(application);
    }

    public void withdrawApplication(User seeker, Long applicationId) {
        JobApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        if (!application.getSeeker().getId().equals(seeker.getId())) {
            throw new BadRequestException("You can only withdraw your own applications");
        }

        applicationRepository.delete(application);
    }

    private PageResponse<ApplicationDto> toPageResponse(Page<JobApplication> page) {
        return PageResponse.<ApplicationDto>builder()
                .content(page.getContent().stream().map(this::mapToDto).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private ApplicationDto mapToDto(JobApplication app) {
        return ApplicationDto.builder()
                .id(app.getId())
                .jobId(app.getJob().getId())
                .jobTitle(app.getJob().getTitle())
                .companyName(app.getJob().getCompany().getName())
                .seekerId(app.getSeeker().getId())
                .seekerName(app.getSeeker().getName())
                .seekerEmail(app.getSeeker().getEmail())
                .resumeId(app.getResume().getId())
                .resumeUrl(app.getResume().getFileUrl())
                .coverLetter(app.getCoverLetter())
                .status(app.getStatus())
                .appliedAt(app.getAppliedAt())
                .updatedAt(app.getUpdatedAt())
                .build();
    }
}
