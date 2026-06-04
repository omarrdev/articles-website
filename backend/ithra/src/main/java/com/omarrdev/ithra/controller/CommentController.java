package com.omarrdev.ithra.controller;

import com.omarrdev.ithra.dto.request.CommentRequest;
import com.omarrdev.ithra.dto.response.CommentResponse;
import com.omarrdev.ithra.dto.response.PageResponse;
import com.omarrdev.ithra.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Comment management")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @Operation(summary = "Get comments for an article")
    public PageResponse<CommentResponse> getComments(
            @PathVariable Long articleId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return commentService.getComments(articleId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Add a comment", security = @SecurityRequirement(name = "bearerAuth"))
    public CommentResponse addComment(@PathVariable Long articleId,
                                      @Valid @RequestBody CommentRequest request) {
        return commentService.addComment(articleId, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete a comment", security = @SecurityRequirement(name = "bearerAuth"))
    public void deleteComment(@PathVariable Long articleId, @PathVariable Long id) {
        commentService.deleteComment(articleId, id);
    }
}
