package com.omarrdev.ithra.controller;

import com.omarrdev.ithra.dto.response.stats.*;
import com.omarrdev.ithra.service.AdminStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
@Tag(name = "Admin — Stats", description = "Platform statistics")
@SecurityRequirement(name = "bearerAuth")
public class AdminStatsController {

    private final AdminStatsService statsService;

    @GetMapping("/overview")
    @Operation(summary = "Overview: totals for articles, users, views, likes, comments")
    public StatsOverviewResponse getOverview() {
        return statsService.getOverview();
    }

    @GetMapping("/articles-over-time")
    @Operation(summary = "Articles published per month (last 12 months)")
    public List<MonthlyStatResponse> getArticlesOverTime() {
        return statsService.getArticlesOverTime();
    }

    @GetMapping("/views-over-time")
    @Operation(summary = "Views per month (last 12 months, grouped by publish date)")
    public List<MonthlyStatResponse> getViewsOverTime() {
        return statsService.getViewsOverTime();
    }

    @GetMapping("/top-articles")
    @Operation(summary = "Top 10 articles by view count")
    public List<TopArticleResponse> getTopArticles() {
        return statsService.getTopArticles();
    }

    @GetMapping("/top-categories")
    @Operation(summary = "Categories ranked by article count")
    public List<TopCategoryResponse> getTopCategories() {
        return statsService.getTopCategories();
    }
}
