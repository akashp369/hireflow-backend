package com.hireflow.user.dto;

import com.hireflow.common.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private String profilePhotoUrl;
    private String phone;
    private LocalDateTime createdAt;
}
