package com.omarrdev.ithra.repository;

import com.omarrdev.ithra.entity.Article;
import com.omarrdev.ithra.entity.User;
import com.omarrdev.ithra.enums.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findByStatusAndDeletedFalse(ArticleStatus status, Pageable pageable);

    @Query("SELECT a FROM Article a " +
           "JOIN FETCH a.author " +
           "LEFT JOIN FETCH a.category c " +
           "WHERE a.status = 'PUBLISHED' AND a.deleted = false " +
           "AND (:categorySlug IS NULL OR c.slug = :categorySlug) " +
           "AND (:search IS NULL OR LOWER(a.title) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Article> findPublishedWithFilters(
            @Param("categorySlug") String categorySlug,
            @Param("search") String search,
            Pageable pageable);

    Optional<Article> findBySlugAndDeletedFalse(String slug);

    Optional<Article> findByIdAndDeletedFalse(Long id);

    Page<Article> findByAuthorAndDeletedFalse(User author, Pageable pageable);

    Page<Article> findByDeletedFalse(Pageable pageable);

    boolean existsBySlug(String slug);

    long countByDeletedFalse();

    long countByStatusAndDeletedFalse(ArticleStatus status);

    @Query("SELECT YEAR(a.publishedAt) as yr, MONTH(a.publishedAt) as mo, COUNT(a) as cnt " +
           "FROM Article a WHERE a.status = 'PUBLISHED' AND a.deleted = false AND a.publishedAt >= :since " +
           "GROUP BY YEAR(a.publishedAt), MONTH(a.publishedAt) ORDER BY yr, mo")
    List<Object[]> countPublishedByMonth(@Param("since") LocalDateTime since);

    @Query("SELECT YEAR(a.publishedAt) as yr, MONTH(a.publishedAt) as mo, SUM(a.viewCount) as views " +
           "FROM Article a WHERE a.status = 'PUBLISHED' AND a.deleted = false AND a.publishedAt >= :since " +
           "GROUP BY YEAR(a.publishedAt), MONTH(a.publishedAt) ORDER BY yr, mo")
    List<Object[]> sumViewsByMonth(@Param("since") LocalDateTime since);

    @Query("SELECT a FROM Article a WHERE a.deleted = false AND a.status = 'PUBLISHED' ORDER BY a.viewCount DESC")
    List<Article> findTopByViews(Pageable pageable);

    @Query("SELECT a.category.id, COUNT(a) FROM Article a " +
           "WHERE a.deleted = false AND a.status = 'PUBLISHED' AND a.category IS NOT NULL " +
           "GROUP BY a.category.id")
    List<Object[]> countPublishedGroupedByCategoryId();

    @Query("SELECT COUNT(a) FROM Article a " +
           "WHERE a.category.id = :categoryId AND a.deleted = false AND a.status = 'PUBLISHED'")
    long countPublishedByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT c.name, c.slug, COUNT(a) FROM Article a JOIN a.category c " +
           "WHERE a.deleted = false AND a.status = 'PUBLISHED' " +
           "GROUP BY c.id, c.name, c.slug ORDER BY COUNT(a) DESC")
    List<Object[]> countArticlesByCategory();
}
