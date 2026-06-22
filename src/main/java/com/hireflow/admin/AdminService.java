package com.hireflow.admin;

import com.hireflow.admin.dto.DashboardDto;
import com.hireflow.application.JobApplicationRepository;
import com.hireflow.application.dto.ApplicationDto;
import com.hireflow.common.PageResponse;
import com.hireflow.common.enums.Role;
import com.hireflow.common.exception.ResourceNotFoundException;
import com.hireflow.application.JobApplication;
import com.hireflow.company.CompanyRepository;
import com.hireflow.job.Job;
import com.hireflow.job.JobRepository;
import com.hireflow.job.dto.JobDto;
import com.hireflow.user.User;
import com.hireflow.user.UserRepository;
import com.hireflow.user.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final JobApplicationRepository applicationRepository;
    private final CompanyRepository companyRepository;

    public PageResponse<UserProfileDto> getAllUsers(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return PageResponse.<UserProfileDto>builder()
                .content(page.getContent().stream().map(this::mapUserToDto).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public PageResponse<JobDto> getAllJobs(Pageable pageable) {
        Page<Job> page = jobRepository.findAll(pageable);
        return PageResponse.<JobDto>builder()
                .content(page.getContent().stream().map(this::mapJobToDto).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new ResourceNotFoundException("Job not found");
        }
        jobRepository.deleteById(id);
    }

    public PageResponse<ApplicationDto> getAllApplications(Pageable pageable) {
        Page<JobApplication> page = applicationRepository.findAll(pageable);
        return PageResponse.<ApplicationDto>builder()
                .content(page.getContent().stream().map(this::mapAppToDto).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public DashboardDto getDashboard() {
        long totalUsers = userRepository.count();
        long totalSeekers = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.SEEKER).count();
        long totalRecruiters = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.RECRUITER).count();
        long totalJobs = jobRepository.count();
        long activeJobs = jobRepository.findAll().stream()
                .filter(Job::getIsActive).count();
        long totalApplications = applicationRepository.count();

        return DashboardDto.builder()
                .totalUsers(totalUsers)
                .totalSeekers(totalSeekers)
                .totalRecruiters(totalRecruiters)
                .totalJobs(totalJobs)
                .activeJobs(activeJobs)
                .totalApplications(totalApplications)
                .totalCompanies(companyRepository.count())
                .build();
    }

    private UserProfileDto mapUserToDto(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .profilePhotoUrl(user.getProfilePhotoUrl())
                .phone(user.getPhone())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private JobDto mapJobToDto(Job job) {
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
                .postedById(job.getPostedBy().getId())
                .postedByName(job.getPostedBy().getName())
                .createdAt(job.getCreatedAt())
                .deadline(job.getDeadline())
                .build();
    }

    private ApplicationDto mapAppToDto(JobApplication app) {
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
