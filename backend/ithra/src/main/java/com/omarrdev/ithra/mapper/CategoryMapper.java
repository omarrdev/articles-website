package com.omarrdev.ithra.mapper;

import com.omarrdev.ithra.dto.response.CategoryResponse;
import com.omarrdev.ithra.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "articleCount", constant = "0L")
    CategoryResponse toResponse(Category category);

    @Mapping(target = "articleCount", constant = "0L")
    List<CategoryResponse> toResponseList(List<Category> categories);
}
