package com.omarrdev.ithra.service;

import com.omarrdev.ithra.dto.request.UpdateRoleRequest;
import com.omarrdev.ithra.dto.response.PageResponse;
import com.omarrdev.ithra.dto.response.UserResponse;
import com.omarrdev.ithra.exception.ResourceNotFoundException;
import com.omarrdev.ithra.mapper.UserMapper;
import com.omarrdev.ithra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public PageResponse<UserResponse> getUsers(Pageable pageable) {
        return PageResponse.of(userRepository.findAllByDeletedFalse(pageable), userMapper::toResponse);
    }

    public UserResponse getUser(Long id) {
        return userMapper.toResponse(
                userRepository.findByIdAndDeletedFalse(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"))
        );
    }

    @Transactional
    public UserResponse updateRole(Long id, UpdateRoleRequest request) {
        var user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(request.getRole());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        var user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setDeleted(true);
        userRepository.save(user);
    }
}
