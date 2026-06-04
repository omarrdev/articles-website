package com.omarrdev.ithra.mapper;

import com.omarrdev.ithra.dto.response.UserResponse;
import com.omarrdev.ithra.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
}
