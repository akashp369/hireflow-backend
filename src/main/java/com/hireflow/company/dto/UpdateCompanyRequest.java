package com.hireflow.company.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompanyRequest {

    private String name;
    private String description;
    private String website;
    private String location;
}
