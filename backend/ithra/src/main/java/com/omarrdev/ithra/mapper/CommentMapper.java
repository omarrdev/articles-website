package com.omarrdev.ithra.mapper;

import com.omarrdev.ithra.dto.response.CommentResponse;
import com.omarrdev.ithra.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {
    @Mapping(source = "author", target = "author")
    CommentResponse toResponse(Comment comment);
}
