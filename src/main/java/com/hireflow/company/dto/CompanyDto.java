package com.hireflow.company.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    private Long id;
    private String name;
    private String description;
    private String website;
    private String logoUrl;
    private String location;
    private Long recruiterId;
    private String recruiterName;
    private LocalDateTime createdAt;
}
