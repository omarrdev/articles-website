package com.omarrdev.ithra.mapper;

import com.omarrdev.ithra.dto.response.ArticleResponse;
import com.omarrdev.ithra.dto.response.ArticleSummaryResponse;
import com.omarrdev.ithra.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CategoryMapper.class, TagMapper.class})
public interface ArticleMapper {

    @Mapping(source = "author", target = "author")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "tags", target = "tags")
    ArticleResponse toResponse(Article article);

    @Mapping(source = "author", target = "author")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "tags", target = "tags")
    ArticleSummaryResponse toSummaryResponse(Article article);
}
