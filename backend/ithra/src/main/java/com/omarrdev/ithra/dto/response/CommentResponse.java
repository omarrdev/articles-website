package com.omarrdev.ithra.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        UserResponse author,
        LocalDateTime createdAt
) {}
