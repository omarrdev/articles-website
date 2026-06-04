package com.omarrdev.ithra.dto.response;

import com.omarrdev.ithra.enums.Role;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String email,
        Role role,
        boolean emailVerified,
        LocalDateTime createdAt
) {}
