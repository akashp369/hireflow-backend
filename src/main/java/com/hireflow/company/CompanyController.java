package com.hireflow.company;

import com.hireflow.common.ApiResponse;
import com.hireflow.common.PageResponse;
import com.hireflow.company.dto.CompanyDto;
import com.hireflow.company.dto.CreateCompanyRequest;
import com.hireflow.company.dto.UpdateCompanyRequest;
import com.hireflow.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<CompanyDto>> createCompany(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreateCompanyRequest request) {
        CompanyDto company = companyService.createCompany(user, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Company created", company));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<CompanyDto>> getMyCompany(@AuthenticationPrincipal User user) {
        CompanyDto company = companyService.getMyCompany(user);
        return ResponseEntity.ok(ApiResponse.success("Company fetched", company));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<CompanyDto>> updateMyCompany(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateCompanyRequest request) {
        CompanyDto company = companyService.updateMyCompany(user, request);
        return ResponseEntity.ok(ApiResponse.success("Company updated", company));
    }

    @PostMapping("/me/logo")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<ApiResponse<CompanyDto>> uploadLogo(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file) {
        CompanyDto company = companyService.uploadLogo(user, file);
        return ResponseEntity.ok(ApiResponse.success("Logo uploaded", company));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CompanyDto>>> getAllCompanies(Pageable pageable) {
        PageResponse<CompanyDto> companies = companyService.getAllCompanies(pageable);
        return ResponseEntity.ok(ApiResponse.success("Companies fetched", companies));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyDto>> getCompanyById(@PathVariable Long id) {
        CompanyDto company = companyService.getCompanyById(id);
        return ResponseEntity.ok(ApiResponse.success("Company fetched", company));
    }
}
