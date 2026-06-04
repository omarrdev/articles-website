package com.omarrdev.ithra.service;

import com.omarrdev.ithra.dto.response.stats.*;
import com.omarrdev.ithra.enums.ArticleStatus;
import com.omarrdev.ithra.repository.ArticleRepository;
import com.omarrdev.ithra.repository.CommentRepository;
import com.omarrdev.ithra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public StatsOverviewResponse getOverview() {
        long totalArticles = articleRepository.countByDeletedFalse();
        long publishedArticles = articleRepository.countByStatusAndDeletedFalse(ArticleStatus.PUBLISHED);
        long totalUsers = userRepository.count();
        long totalViews = articleRepository.findAll().stream()
                .filter(a -> !a.isDeleted())
                .mapToLong(a -> a.getViewCount())
                .sum();
        long totalLikes = articleRepository.findAll().stream()
                .filter(a -> !a.isDeleted())
                .mapToLong(a -> a.getLikeCount())
                .sum();
        long totalComments = commentRepository.countByDeletedFalse();

        return new StatsOverviewResponse(totalArticles, publishedArticles, totalUsers, totalViews, totalLikes, totalComments);
    }

    public List<MonthlyStatResponse> getArticlesOverTime() {
        LocalDateTime since = LocalDateTime.now().minusMonths(12);
        return articleRepository.countPublishedByMonth(since).stream()
                .map(row -> new MonthlyStatResponse(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).intValue(),
                        ((Number) row[2]).longValue()))
                .toList();
    }

    public List<MonthlyStatResponse> getViewsOverTime() {
        LocalDateTime since = LocalDateTime.now().minusMonths(12);
        return articleRepository.sumViewsByMonth(since).stream()
                .map(row -> new MonthlyStatResponse(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).intValue(),
                        ((Number) row[2]).longValue()))
                .toList();
    }

    public List<TopArticleResponse> getTopArticles() {
        return articleRepository.findTopByViews(PageRequest.of(0, 10)).stream()
                .map(a -> new TopArticleResponse(a.getId(), a.getTitle(), a.getSlug(), a.getViewCount(), a.getLikeCount()))
                .toList();
    }

    public List<TopCategoryResponse> getTopCategories() {
        return articleRepository.countArticlesByCategory().stream()
                .map(row -> new TopCategoryResponse(
                        (String) row[0],
                        (String) row[1],
                        ((Number) row[2]).longValue()))
                .toList();
    }
}
