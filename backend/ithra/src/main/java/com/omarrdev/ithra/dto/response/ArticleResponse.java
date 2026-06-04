package com.omarrdev.ithra.dto.response;

import com.omarrdev.ithra.enums.ArticleStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleResponse(
        Long id,
        String title,
        String slug,
        String content,
        String summary,
        String coverImageUrl,
        ArticleStatus status,
        long viewCount,
        long likeCount,
        UserResponse author,
        CategoryResponse category,
        List<TagResponse> tags,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
