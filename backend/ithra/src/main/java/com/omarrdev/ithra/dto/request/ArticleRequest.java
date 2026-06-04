package com.omarrdev.ithra.dto.request;

import com.omarrdev.ithra.enums.ArticleStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ArticleRequest {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private String summary;
    private String coverImageUrl;
    private ArticleStatus status;
    private Long categoryId;
    private List<Long> tagIds;
}
