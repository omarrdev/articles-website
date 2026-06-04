package com.omarrdev.ithra.controller;

import com.omarrdev.ithra.dto.request.ArticleRequest;
import com.omarrdev.ithra.dto.response.ArticleResponse;
import com.omarrdev.ithra.dto.response.ArticleSummaryResponse;
import com.omarrdev.ithra.dto.response.LikeResponse;
import com.omarrdev.ithra.dto.response.PageResponse;
import com.omarrdev.ithra.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Tag(name = "Articles", description = "Article management")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    @Operation(summary = "Get published articles (paginated, filterable)")
    public PageResponse<ArticleSummaryResponse> getPublished(
            @PageableDefault(size = 10, sort = "publishedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {
        return articleService.getPublishedArticles(category, search, pageable);
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Get article by slug")
    public ArticleResponse getBySlug(@PathVariable String slug,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        return articleService.getBySlug(slug, request, response);
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    @Operation(summary = "Get article by id for editing (any status, owner/admin only)", security = @SecurityRequirement(name = "bearerAuth"))
    public ArticleResponse getForEdit(@PathVariable Long id) {
        return articleService.getForEdit(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    @Operation(summary = "Create article", security = @SecurityRequirement(name = "bearerAuth"))
    public ArticleResponse createArticle(@Valid @RequestBody ArticleRequest request) {
        return articleService.createArticle(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    @Operation(summary = "Update article", security = @SecurityRequirement(name = "bearerAuth"))
    public ArticleResponse updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleRequest request) {
        return articleService.updateArticle(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    @Operation(summary = "Delete article (soft)", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }

    @GetMapping("/my")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get my articles", security = @SecurityRequirement(name = "bearerAuth"))
    public PageResponse<ArticleSummaryResponse> getMyArticles(
            @PageableDefault(size = 100, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return articleService.getMyArticles(pageable);
    }

    @PatchMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('AUTHOR', 'ADMIN')")
    @Operation(summary = "Publish article", security = @SecurityRequirement(name = "bearerAuth"))
    public ArticleResponse publishArticle(@PathVariable Long id) {
        return articleService.publishArticle(id);
    }

    @PatchMapping("/{id}/hide")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Hide article (set back to DRAFT)", security = @SecurityRequirement(name = "bearerAuth"))
    public ArticleResponse hideArticle(@PathVariable Long id) {
        return articleService.hideArticle(id);
    }

    @PostMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Toggle like", security = @SecurityRequirement(name = "bearerAuth"))
    public LikeResponse toggleLike(@PathVariable Long id) {
        return articleService.toggleLike(id);
    }

    @GetMapping("/{id}/likes")
    @Operation(summary = "Get like count and isLiked")
    public LikeResponse getLikes(@PathVariable Long id) {
        return articleService.getLikes(id);
    }
}
