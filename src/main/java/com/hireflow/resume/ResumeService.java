package com.hireflow.resume;

import com.hireflow.common.exception.ResourceNotFoundException;
import com.hireflow.config.CloudinaryService;
import com.hireflow.resume.dto.ResumeDto;
import com.hireflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final CloudinaryService cloudinaryService;

    public ResumeDto uploadResume(User user, MultipartFile file) {
        String url = cloudinaryService.uploadFile(file, "hireflow/resumes");

        boolean isFirst = resumeRepository.findByUserId(user.getId()).isEmpty();

        Resume resume = Resume.builder()
                .user(user)
                .fileUrl(url)
                .fileName(file.getOriginalFilename())
                .isDefault(isFirst)
                .build();

        resumeRepository.save(resume);
        return mapToDto(resume);
    }

    public List<ResumeDto> getMyResumes(User user) {
        return resumeRepository.findByUserId(user.getId())
                .stream().map(this::mapToDto).toList();
    }

    public ResumeDto setDefault(User user, Long resumeId) {
        Resume resume = resumeRepository.findByIdAndUserId(resumeId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        resumeRepository.findByUserIdAndIsDefaultTrue(user.getId())
                .ifPresent(old -> {
                    old.setIsDefault(false);
                    resumeRepository.save(old);
                });

        resume.setIsDefault(true);
        resumeRepository.save(resume);
        return mapToDto(resume);
    }

    public void deleteResume(User user, Long resumeId) {
        Resume resume = resumeRepository.findByIdAndUserId(resumeId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
        resumeRepository.delete(resume);
    }

    private ResumeDto mapToDto(Resume resume) {
        return ResumeDto.builder()
                .id(resume.getId())
                .fileUrl(resume.getFileUrl())
                .fileName(resume.getFileName())
                .isDefault(resume.getIsDefault())
                .uploadedAt(resume.getUploadedAt())
                .build();
    }
}
