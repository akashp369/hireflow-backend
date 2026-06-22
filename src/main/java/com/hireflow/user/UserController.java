package com.hireflow.user;

import com.hireflow.common.ApiResponse;
import com.hireflow.common.PageResponse;
import com.hireflow.user.dto.UpdateProfileRequest;
import com.hireflow.user.dto.UserProfileDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> getMyProfile(@AuthenticationPrincipal User user) {
        UserProfileDto profile = userService.getProfile(user);
        return ResponseEntity.ok(ApiResponse.success("Profile fetched", profile));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileDto>> updateMyProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdateProfileRequest request) {
        UserProfileDto profile = userService.updateProfile(user, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated", profile));
    }

    @PostMapping("/me/photo")
    public ResponseEntity<ApiResponse<UserProfileDto>> uploadProfilePhoto(
            @AuthenticationPrincipal User user,
            @RequestParam("file") MultipartFile file) {
        UserProfileDto profile = userService.uploadProfilePhoto(user, file);
        return ResponseEntity.ok(ApiResponse.success("Photo uploaded", profile));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserProfileDto>> getUserById(@PathVariable Long id) {
        UserProfileDto profile = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User fetched", profile));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<UserProfileDto>>> getAllUsers(Pageable pageable) {
        PageResponse<UserProfileDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success("Users fetched", users));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted"));
    }
}
