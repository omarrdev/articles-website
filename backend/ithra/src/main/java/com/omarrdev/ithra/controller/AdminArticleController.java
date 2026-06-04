package com.omarrdev.ithra.controller;

import com.omarrdev.ithra.dto.response.ArticleSummaryResponse;
import com.omarrdev.ithra.dto.response.PageResponse;
import com.omarrdev.ithra.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/articles")
@RequiredArgsConstructor
@Tag(name = "Admin — Articles", description = "Admin article management")
@SecurityRequirement(name = "bearerAuth")
public class AdminArticleController {

    private final ArticleService articleService;

    @GetMapping
    @Operation(summary = "List all articles (paginated, all statuses)")
    public PageResponse<ArticleSummaryResponse> getAllArticles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return articleService.getAllArticlesForAdmin(
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete article (soft)")
    public void deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
    }
}
