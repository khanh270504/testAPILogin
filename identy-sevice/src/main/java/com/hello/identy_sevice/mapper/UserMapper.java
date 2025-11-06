package com.hello.identy_sevice.mapper;

import com.hello.identy_sevice.dto.request.UserCreationRequest;
import com.hello.identy_sevice.dto.request.UserUpdateRequest;
import com.hello.identy_sevice.dto.response.UserResponse;
import com.hello.identy_sevice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
