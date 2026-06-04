package com.omarrdev.ithra.controller;

import com.omarrdev.ithra.dto.request.TagRequest;
import com.omarrdev.ithra.dto.response.TagResponse;
import com.omarrdev.ithra.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Tag(name = "Tags", description = "Tag management")
public class TagController {

    private final TagService tagService;

    @GetMapping
    @Operation(summary = "Get all tags")
    public List<TagResponse> getAll() {
        return tagService.getAll();
    }

    @GetMapping("/{slug}")
    @Operation(summary = "Get tag by slug")
    public TagResponse getBySlug(@PathVariable String slug) {
        return tagService.getBySlug(slug);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create tag", security = @SecurityRequirement(name = "bearerAuth"))
    public TagResponse create(@Valid @RequestBody TagRequest request) {
        return tagService.create(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update tag", security = @SecurityRequirement(name = "bearerAuth"))
    public TagResponse update(@PathVariable Long id, @Valid @RequestBody TagRequest request) {
        return tagService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete tag", security = @SecurityRequirement(name = "bearerAuth"))
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }
}
