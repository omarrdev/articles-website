package com.omarrdev.ithra.dto.response.stats;

public record TopCategoryResponse(
        String name,
        String slug,
        long articleCount
) {}
