package com.omarrdev.ithra.service;

import com.omarrdev.ithra.dto.request.ArticleRequest;
import com.omarrdev.ithra.dto.response.ArticleResponse;
import com.omarrdev.ithra.dto.response.ArticleSummaryResponse;
import com.omarrdev.ithra.dto.response.LikeResponse;
import com.omarrdev.ithra.dto.response.PageResponse;
import com.omarrdev.ithra.entity.*;
import com.omarrdev.ithra.enums.ArticleStatus;
import com.omarrdev.ithra.exception.BusinessException;
import com.omarrdev.ithra.exception.ResourceNotFoundException;
import com.omarrdev.ithra.mapper.ArticleMapper;
import com.omarrdev.ithra.repository.*;
import com.omarrdev.ithra.util.SlugUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ArticleMapper articleMapper;

    @Transactional(readOnly = true)
    public PageResponse<ArticleSummaryResponse> getPublishedArticles(
            String categorySlug, String search, Pageable pageable) {
        Page<Article> page = articleRepository.findPublishedWithFilters(
                categorySlug != null && !categorySlug.isBlank() ? categorySlug : null,
                search != null && !search.isBlank() ? search : null,
                pageable);
        return PageResponse.of(page, articleMapper::toSummaryResponse);
    }

    @Transactional
    public ArticleResponse getBySlug(String slug, HttpServletRequest request, HttpServletResponse response) {
        Article article = articleRepository.findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found: " + slug));

        if (article.getStatus() != ArticleStatus.PUBLISHED) {
            throw new ResourceNotFoundException("Article not found: " + slug);
        }

        String cookieName = "viewed_" + slug;
        boolean alreadyViewed = false;
        if (request.getCookies() != null) {
            alreadyViewed = Arrays.stream(request.getCookies())
                    .anyMatch(c -> c.getName().equals(cookieName));
        }

        if (!alreadyViewed) {
            article.setViewCount(article.getViewCount() + 1);
            articleRepository.save(article);
            Cookie cookie = new Cookie(cookieName, "1");
            cookie.setMaxAge(86400);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }

        return articleMapper.toResponse(article);
    }

    @Transactional(readOnly = true)
    public ArticleResponse getForEdit(Long id) {
        return articleMapper.toResponse(getArticleForEdit(id));
    }

    @Transactional
    public ArticleResponse createArticle(ArticleRequest request) {
        User author = getCurrentUser();
        if (!author.isEmailVerified()) {
            throw new BusinessException("Email must be verified to create articles");
        }

        Article article = Article.builder()
                .title(request.getTitle())
                .slug(generateUniqueSlug(request.getTitle()))
                .content(request.getContent())
                .summary(request.getSummary())
                .coverImageUrl(request.getCoverImageUrl())
                .status(request.getStatus() != null ? request.getStatus() : ArticleStatus.DRAFT)
                .author(author)
                .build();

        applyCategory(article, request.getCategoryId());
        applyTags(article, request.getTagIds());

        if (article.getStatus() == ArticleStatus.PUBLISHED) {
            article.setPublishedAt(LocalDateTime.now());
        }

        return articleMapper.toResponse(articleRepository.save(article));
    }

    @Transactional
    public ArticleResponse updateArticle(Long id, ArticleRequest request) {
        Article article = getArticleForEdit(id);

        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        article.setSummary(request.getSummary());
        article.setCoverImageUrl(request.getCoverImageUrl());

        if (request.getStatus() != null && request.getStatus() != article.getStatus()) {
            article.setStatus(request.getStatus());
            if (request.getStatus() == ArticleStatus.PUBLISHED && article.getPublishedAt() == null) {
                article.setPublishedAt(LocalDateTime.now());
            }
        }

        applyCategory(article, request.getCategoryId());
        applyTags(article, request.getTagIds());

        return articleMapper.toResponse(articleRepository.save(article));
    }

    @Transactional
    public void deleteArticle(Long id) {
        Article article = getArticleForEdit(id);
        article.setDeleted(true);
        articleRepository.save(article);
    }

    @Transactional(readOnly = true)
    public PageResponse<ArticleSummaryResponse> getAllArticlesForAdmin(Pageable pageable) {
        Page<Article> page = articleRepository.findByDeletedFalse(pageable);
        return PageResponse.of(page, articleMapper::toSummaryResponse);
    }

    @Transactional(readOnly = true)
    public PageResponse<ArticleSummaryResponse> getMyArticles(Pageable pageable) {
        User author = getCurrentUser();
        Page<Article> page = articleRepository.findByAuthorAndDeletedFalse(author, pageable);
        return PageResponse.of(page, articleMapper::toSummaryResponse);
    }

    @Transactional
    public ArticleResponse publishArticle(Long id) {
        Article article = getArticleForEdit(id);
        article.setStatus(ArticleStatus.PUBLISHED);
        if (article.getPublishedAt() == null) {
            article.setPublishedAt(LocalDateTime.now());
        }
        return articleMapper.toResponse(articleRepository.save(article));
    }

    @Transactional
    public ArticleResponse hideArticle(Long id) {
        Article article = articleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        article.setStatus(ArticleStatus.DRAFT);
        return articleMapper.toResponse(articleRepository.save(article));
    }

    @Transactional
    public LikeResponse toggleLike(Long id) {
        Article article = articleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        User user = getCurrentUser();

        articleLikeRepository.findByArticleAndUser(article, user).ifPresentOrElse(
                like -> {
                    articleLikeRepository.delete(like);
                    article.setLikeCount(Math.max(0, article.getLikeCount() - 1));
                },
                () -> {
                    articleLikeRepository.save(ArticleLike.builder().article(article).user(user).build());
                    article.setLikeCount(article.getLikeCount() + 1);
                }
        );

        articleRepository.save(article);
        boolean liked = articleLikeRepository.existsByArticleAndUser(article, user);
        return new LikeResponse(article.getLikeCount(), liked);
    }

    @Transactional(readOnly = true)
    public LikeResponse getLikes(Long id) {
        Article article = articleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        boolean liked = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            userRepository.findByUsernameAndDeletedFalse(auth.getName())
                    .ifPresent(u -> {});
            User user = userRepository.findByUsernameAndDeletedFalse(auth.getName()).orElse(null);
            if (user != null) {
                liked = articleLikeRepository.existsByArticleAndUser(article, user);
            }
        }

        return new LikeResponse(article.getLikeCount(), liked);
    }

    private Article getArticleForEdit(Long id) {
        Article article = articleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));

        User currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
        boolean isOwner = article.getAuthor().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new BusinessException("You do not have permission to edit this article", HttpStatus.FORBIDDEN);
        }

        return article;
    }

    private String generateUniqueSlug(String title) {
        String base = SlugUtil.toSlug(title);
        String slug = base;
        int counter = 1;
        while (articleRepository.existsBySlug(slug)) {
            slug = base + "-" + counter++;
        }
        return slug;
    }

    private void applyCategory(Article article, Long categoryId) {
        if (categoryId != null) {
            Category category = categoryRepository.findByIdAndDeletedFalse(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            article.setCategory(category);
        } else {
            article.setCategory(null);
        }
    }

    private void applyTags(Article article, List<Long> tagIds) {
        if (tagIds != null && !tagIds.isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(tagIds);
            article.setTags(tags);
        } else {
            article.getTags().clear();
        }
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new BusinessException("Authenticated user not found", HttpStatus.UNAUTHORIZED));
    }
}
