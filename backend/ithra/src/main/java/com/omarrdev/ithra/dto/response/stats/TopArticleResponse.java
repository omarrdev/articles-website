package com.omarrdev.ithra.dto.response.stats;

public record TopArticleResponse(
        Long id,
        String title,
        String slug,
        long viewCount,
        long likeCount
) {}
