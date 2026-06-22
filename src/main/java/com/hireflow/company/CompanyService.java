package com.hireflow.company;

import com.hireflow.common.PageResponse;
import com.hireflow.common.exception.BadRequestException;
import com.hireflow.common.exception.ResourceNotFoundException;
import com.hireflow.company.dto.CompanyDto;
import com.hireflow.company.dto.CreateCompanyRequest;
import com.hireflow.company.dto.UpdateCompanyRequest;
import com.hireflow.config.CloudinaryService;
import com.hireflow.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CloudinaryService cloudinaryService;

    public CompanyDto createCompany(User recruiter, CreateCompanyRequest request) {
        if (companyRepository.existsByRecruiterId(recruiter.getId())) {
            throw new BadRequestException("You already have a company registered");
        }

        Company company = Company.builder()
                .name(request.getName())
                .description(request.getDescription())
                .website(request.getWebsite())
                .location(request.getLocation())
                .recruiter(recruiter)
                .build();

        companyRepository.save(company);
        return mapToDto(company);
    }

    public CompanyDto getMyCompany(User recruiter) {
        Company company = companyRepository.findByRecruiterId(recruiter.getId())
                .orElseThrow(() -> new ResourceNotFoundException("You don't have a company registered"));
        return mapToDto(company);
    }

    public CompanyDto updateMyCompany(User recruiter, UpdateCompanyRequest request) {
        Company company = companyRepository.findByRecruiterId(recruiter.getId())
                .orElseThrow(() -> new ResourceNotFoundException("You don't have a company registered"));

        if (request.getName() != null) company.setName(request.getName());
        if (request.getDescription() != null) company.setDescription(request.getDescription());
        if (request.getWebsite() != null) company.setWebsite(request.getWebsite());
        if (request.getLocation() != null) company.setLocation(request.getLocation());

        companyRepository.save(company);
        return mapToDto(company);
    }

    public CompanyDto uploadLogo(User recruiter, MultipartFile file) {
        Company company = companyRepository.findByRecruiterId(recruiter.getId())
                .orElseThrow(() -> new ResourceNotFoundException("You don't have a company registered"));

        String url = cloudinaryService.uploadFile(file, "hireflow/company-logos");
        company.setLogoUrl(url);
        companyRepository.save(company);
        return mapToDto(company);
    }

    public PageResponse<CompanyDto> getAllCompanies(Pageable pageable) {
        Page<Company> page = companyRepository.findAll(pageable);
        return PageResponse.<CompanyDto>builder()
                .content(page.getContent().stream().map(this::mapToDto).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public CompanyDto getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        return mapToDto(company);
    }

    private CompanyDto mapToDto(Company company) {
        return CompanyDto.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .website(company.getWebsite())
                .logoUrl(company.getLogoUrl())
                .location(company.getLocation())
                .recruiterId(company.getRecruiter().getId())
                .recruiterName(company.getRecruiter().getName())
                .createdAt(company.getCreatedAt())
                .build();
    }
}
