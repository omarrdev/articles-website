package com.omarrdev.ithra.dto.response.stats;

public record StatsOverviewResponse(
        long totalArticles,
        long publishedArticles,
        long totalUsers,
        long totalViews,
        long totalLikes,
        long totalComments
) {}
