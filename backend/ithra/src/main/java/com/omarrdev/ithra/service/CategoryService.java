package com.omarrdev.ithra.service;

import com.omarrdev.ithra.dto.request.CategoryRequest;
import com.omarrdev.ithra.dto.response.CategoryResponse;
import com.omarrdev.ithra.entity.Category;
import com.omarrdev.ithra.exception.BusinessException;
import com.omarrdev.ithra.exception.ResourceNotFoundException;
import com.omarrdev.ithra.repository.ArticleRepository;
import com.omarrdev.ithra.repository.CategoryRepository;
import com.omarrdev.ithra.util.SlugUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    public List<CategoryResponse> getAll() {
        Map<Long, Long> countMap = articleRepository.countPublishedGroupedByCategoryId()
                .stream()
                .collect(Collectors.toMap(
                        row -> (Long) row[0],
                        row -> (Long) row[1]
                ));
        return categoryRepository.findAllByDeletedFalse().stream()
                .map(c -> toResponse(c, countMap.getOrDefault(c.getId(), 0L)))
                .toList();
    }

    public CategoryResponse getBySlug(String slug) {
        Category category = categoryRepository.findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + slug));
        long count = articleRepository.countPublishedByCategoryId(category.getId());
        return toResponse(category, count);
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNameAndDeletedFalse(request.getName())) {
            throw new BusinessException("Category name already exists");
        }
        Category category = Category.builder()
                .name(request.getName())
                .slug(SlugUtil.toSlug(request.getName()))
                .description(request.getDescription())
                .build();
        return toResponse(categoryRepository.save(category), 0L);
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setName(request.getName());
        category.setSlug(SlugUtil.toSlug(request.getName()));
        category.setDescription(request.getDescription());
        long count = articleRepository.countPublishedByCategoryId(id);
        return toResponse(categoryRepository.save(category), count);
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        category.setDeleted(true);
        categoryRepository.save(category);
    }

    private CategoryResponse toResponse(Category c, long articleCount) {
        return new CategoryResponse(c.getId(), c.getName(), c.getSlug(), c.getDescription(), articleCount);
    }
}
