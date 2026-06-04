package com.omarrdev.ithra.dto.response;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description,
        long articleCount
) {}
