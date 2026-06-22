package com.hireflow.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDto {

    private Long id;
    private String fileUrl;
    private String fileName;
    private Boolean isDefault;
    private LocalDateTime uploadedAt;
}
