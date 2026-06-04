package com.omarrdev.ithra.mapper;

import com.omarrdev.ithra.dto.response.TagResponse;
import com.omarrdev.ithra.entity.Tag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagResponse toResponse(Tag tag);
    List<TagResponse> toResponseList(List<Tag> tags);
}
