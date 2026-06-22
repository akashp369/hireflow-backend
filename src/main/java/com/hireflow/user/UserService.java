package com.hireflow.user;

import com.hireflow.common.PageResponse;
import com.hireflow.common.exception.ResourceNotFoundException;
import com.hireflow.config.CloudinaryService;
import com.hireflow.user.dto.UpdateProfileRequest;
import com.hireflow.user.dto.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    public UserProfileDto getProfile(User user) {
        return mapToDto(user);
    }

    public UserProfileDto updateProfile(User user, UpdateProfileRequest request) {
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        userRepository.save(user);
        return mapToDto(user);
    }

    public UserProfileDto uploadProfilePhoto(User user, MultipartFile file) {
        String url = cloudinaryService.uploadFile(file, "hireflow/profile-photos");
        user.setProfilePhotoUrl(url);
        userRepository.save(user);
        return mapToDto(user);
    }

    public UserProfileDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToDto(user);
    }

    public PageResponse<UserProfileDto> getAllUsers(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return PageResponse.<UserProfileDto>builder()
                .content(page.getContent().stream().map(this::mapToDto).toList())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserProfileDto mapToDto(User user) {
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
}
